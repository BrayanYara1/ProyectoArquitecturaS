package com.example.gestionturnosapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.local.PreferenceManager
import com.example.gestionturnosapp.data.model.HealthRecord
import com.example.gestionturnosapp.databinding.FragmentHealthStatsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class HealthStatsFragment : Fragment() {

    private var _binding: FragmentHealthStatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthStatsViewModel by viewModels()
    private lateinit var symptomsAdapter: SymptomsAdapter

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.root.setPadding(
                binding.root.paddingLeft,
                systemBars.top,
                binding.root.paddingRight,
                systemBars.bottom
            )
            insets
        }

        setupCharts()
        setupSymptomsList()
        observeViewModel()
        setupListeners()
        calculateBmi()
    }

    private fun setupCharts() {
        configureChart(binding.chartWeight, "Peso")
        configureChart(binding.chartGlucose, "Glucosa")
        configureChart(binding.chartPressure, "Presión")
        setupWaterChart()
        setupAdherenceChart()
    }

    private fun setupWaterChart() {
        binding.chartWater.description.isEnabled = false
        binding.chartWater.xAxis.setDrawGridLines(false)
        binding.chartWater.axisRight.isEnabled = false
    }

    private fun setupAdherenceChart() {
        binding.chartAdherence.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            holeRadius = 40f
            transparentCircleRadius = 45f
            legend.isEnabled = false
        }
    }

    private fun setupSymptomsList() {
        symptomsAdapter = SymptomsAdapter { symptom ->
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.title_delete_record)
                .setMessage(R.string.msg_delete_record_confirm)
                .setPositiveButton(R.string.btn_delete_confirm) { _, _ ->
                    binding.root.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                    viewModel.deleteSymptom(symptom.id)
                }
                .setNegativeButton(R.string.btn_cancel, null)
                .show()
        }
        binding.rvSymptoms.layoutManager = LinearLayoutManager(context)
        binding.rvSymptoms.adapter = symptomsAdapter
    }

    private fun configureChart(chart: LineChart, label: String) {
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.xAxis.setDrawGridLines(false)
        chart.axisRight.isEnabled = false
        chart.animateX(1000)

        chart.setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                if (e == null) return
                val type = when(chart.id) {
                    R.id.chartWeight -> "WEIGHT"
                    R.id.chartGlucose -> "GLUCOSE"
                    else -> "BLOOD_PRESSURE"
                }
                showDeleteRecordDialog(type, e.x.toInt())
            }
            override fun onNothingSelected() {}
        })
    }

    private fun showDeleteRecordDialog(type: String, index: Int) {
        val records = when(type) {
            "WEIGHT" -> viewModel.weightRecords.value
            "GLUCOSE" -> viewModel.glucoseRecords.value
            else -> viewModel.bloodPressureRecords.value
        } ?: return

        if (index >= records.size) return
        val record = records[index]

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_delete_record))
            .setMessage(getString(R.string.msg_delete_record_confirm))
            .setPositiveButton(getString(R.string.btn_delete_confirm)) { _, _ ->
                viewModel.deleteRecord(record.id)
                Toast.makeText(context, getString(R.string.msg_record_deleted), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.weightRecords.observe(viewLifecycleOwner) { records ->
            updateChart(binding.chartWeight, records, getString(R.string.label_weight), ContextCompat.getColor(requireContext(), R.color.primary))
            calculateBmi()
        }

        viewModel.glucoseRecords.observe(viewLifecycleOwner) { records ->
            updateChart(binding.chartGlucose, records, getString(R.string.label_glucose), ContextCompat.getColor(requireContext(), R.color.accent))
        }

        viewModel.bloodPressureRecords.observe(viewLifecycleOwner) { records ->
            updatePressureChart(records)
        }

        viewModel.waterRecords.observe(viewLifecycleOwner) { records ->
            updateWaterChart(records)
        }

        viewModel.medicationLogs.observe(viewLifecycleOwner) { logs ->
            updateAdherenceChart(logs)
        }

        viewModel.adherencePercentage.observe(viewLifecycleOwner) { percentage ->
            binding.tvAdherenceStatus.text = getString(R.string.label_adherence_format, percentage)
            if (percentage < 70) {
                binding.cardAdherenceTip.visibility = View.VISIBLE
                binding.tvAdherenceTip.text = getString(R.string.msg_adherence_tip)
            } else {
                binding.cardAdherenceTip.visibility = View.GONE
            }
        }

        viewModel.symptomRecords.observe(viewLifecycleOwner) { symptoms ->
            symptomsAdapter.submitList(symptoms)
        }

        viewModel.pdfFile.observe(viewLifecycleOwner) { file ->
            if (file != null) {
                sharePdf(file)
                viewModel.clearPdfState()
            }
        }
    }

    private fun updateChart(chart: LineChart, records: List<HealthRecord>, label: String, color: Int) {
        if (records.isEmpty()) {
            chart.clear()
            chart.setNoDataText("Aún no hay datos para mostrar")
            return
        }

        val entries = records.mapIndexed { index, record ->
            Entry(index.toFloat(), record.value)
        }

        val dataSet = LineDataSet(entries, label).apply {
            this.color = color
            setCircleColor(color)
            lineWidth = 2.5f
            circleRadius = 5f
            setDrawCircleHole(true)
            circleHoleRadius = 2.5f
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = color
            fillAlpha = 40
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    private fun updatePressureChart(records: List<HealthRecord>) {
        if (records.isEmpty()) {
            binding.chartPressure.clear()
            binding.chartPressure.setNoDataText("Aún no hay datos de presión")
            return
        }

        val entriesSystolic = records.mapIndexed { index, record ->
            Entry(index.toFloat(), record.value)
        }
        val entriesDiastolic = records.mapIndexed { index, record ->
            Entry(index.toFloat(), record.valueSecondary ?: 0f)
        }

        val setSys = LineDataSet(entriesSystolic, "Sistólica").apply {
            color = ContextCompat.getColor(requireContext(), R.color.error)
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 4f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
        
        val setDia = LineDataSet(entriesDiastolic, "Diastólica").apply {
            color = ContextCompat.getColor(requireContext(), R.color.primary)
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 4f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }

        binding.chartPressure.data = LineData(setSys, setDia)
        binding.chartPressure.invalidate()
    }

    private fun updateWaterChart(records: List<HealthRecord>) {
        if (records.isEmpty()) {
            binding.chartWater.clear()
            binding.chartWater.setNoDataText("Sin datos de hidratación")
            return
        }

        val sdf = java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
        val last7Days = (0..6).map { i ->
            val cal = java.util.Calendar.getInstance()
            cal.add(java.util.Calendar.DAY_OF_YEAR, -i)
            sdf.format(cal.time) to cal.get(java.util.Calendar.DAY_OF_YEAR)
        }.reversed()

        val entries = last7Days.mapIndexed { index, dayInfo ->
            val totalForDay = records.filter {
                val cal = java.util.Calendar.getInstance().apply { timeInMillis = it.date }
                cal.get(java.util.Calendar.DAY_OF_YEAR) == dayInfo.second
            }.sumOf { it.value.toInt() }
            com.github.mikephil.charting.data.BarEntry(index.toFloat(), totalForDay.toFloat())
        }

        val dataSet = com.github.mikephil.charting.data.BarDataSet(entries, "Agua (ml)").apply {
            color = ContextCompat.getColor(requireContext(), R.color.primary)
            valueTextSize = 10f
        }

        binding.chartWater.data = com.github.mikephil.charting.data.BarData(dataSet)
        
        val xAxis = binding.chartWater.xAxis
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(last7Days.map { it.first })
        xAxis.granularity = 1f
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        
        binding.chartWater.invalidate()
    }

    private fun updateAdherenceChart(logs: List<com.example.gestionturnosapp.data.model.MedicationLog>) {
        if (logs.isEmpty()) {
            binding.chartAdherence.clear()
            binding.chartAdherence.setNoDataText("Sin registros de tomas")
            return
        }

        val percentage = viewModel.adherencePercentage.value ?: 0
        val entries = listOf(
            com.github.mikephil.charting.data.PieEntry(percentage.toFloat(), "Cumplido"),
            com.github.mikephil.charting.data.PieEntry((100 - percentage).toFloat(), "Pendiente")
        )

        val dataSet = com.github.mikephil.charting.data.PieDataSet(entries, "").apply {
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.success),
                ContextCompat.getColor(requireContext(), R.color.outline)
            )
            valueTextSize = 12f
            setDrawValues(true)
        }

        binding.chartAdherence.data = com.github.mikephil.charting.data.PieData(dataSet)
        binding.chartAdherence.centerText = "Adherencia\n$percentage%"
        binding.chartAdherence.invalidate()
    }

    private fun setupListeners() {
        binding.btnAddWeight.setOnClickListener { 
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            showAddDialog("WEIGHT") 
        }
        binding.btnAddGlucose.setOnClickListener { 
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            showAddDialog("GLUCOSE") 
        }
        binding.btnAddPressure.setOnClickListener { 
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            showPressureDialog() 
        }
        binding.btnAddSymptom.setOnClickListener { 
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            showSymptomDialog() 
        }
        
        binding.btnUpdateHeight.setOnClickListener { 
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            showHeightDialog() 
        }

        binding.btnExportPdf.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
            viewModel.generatePdf(requireContext())
            Toast.makeText(context, "Generando PDF...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showHeightDialog() {
        val input = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "Altura en metros (ej: 1.75)"
            setText(preferenceManager.getHeight().toString())
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_adjust_height))
            .setView(input)
            .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val h = input.text.toString().toFloatOrNull()
                if (h != null && h > 0.5 && h < 2.5) {
                    preferenceManager.setHeight(h)
                    calculateBmi()
                } else {
                    Toast.makeText(context, getString(R.string.msg_invalid_height), Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun calculateBmi() {
        val height = preferenceManager.getHeight()
        val lastWeight = viewModel.weightRecords.value?.lastOrNull()?.value
        
        if (height > 0 && lastWeight != null) {
            val bmi = lastWeight / (height * height)
            binding.tvBmiValue.text = String.format(java.util.Locale.US, "%.1f", bmi)
            
            val (status, color) = when {
                bmi < 18.5 -> getString(R.string.label_bmi_under) to R.color.accent
                bmi < 25 -> getString(R.string.label_bmi_normal) to R.color.success
                bmi < 30 -> getString(R.string.label_bmi_over) to R.color.warning
                else -> getString(R.string.label_bmi_obese) to R.color.error
            }
            binding.tvBmiStatus.text = status
            binding.tvBmiStatus.setTextColor(ContextCompat.getColor(requireContext(), color))
        } else {
            binding.tvBmiValue.text = "--"
            binding.tvBmiStatus.text = getString(R.string.label_missing_data)
        }
    }

    private fun showSymptomDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val etDesc = EditText(requireContext()).apply {
            hint = getString(R.string.hint_symptom_desc)
        }
        
        val tvIntensity = TextView(requireContext()).apply {
            text = "${getString(R.string.label_intensity)}: 5"
            setPadding(0, 20, 0, 0)
        }
        
        val seekBar = SeekBar(requireContext()).apply {
            max = 10
            progress = 5
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tvIntensity.text = "${getString(R.string.label_intensity)}: $p1"
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
        
        layout.addView(etDesc)
        layout.addView(tvIntensity)
        layout.addView(seekBar)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.btn_add_symptom)
            .setView(layout)
            .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val desc = etDesc.text.toString().trim()
                if (desc.isNotEmpty()) {
                    viewModel.addSymptom(desc, seekBar.progress)
                    Toast.makeText(context, R.string.msg_record_added, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun showAddDialog(type: String) {
        val editText = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = getString(R.string.hint_value)
            setPadding(40, 40, 40, 40)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.btn_add_record))
            .setView(editText)
            .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val value = editText.text.toString().toFloatOrNull()
                if (value != null) {
                    viewModel.addRecord(type, value)
                    Toast.makeText(context, R.string.msg_record_added, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun showPressureDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val etSys = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Sistólica (ej: 120)"
        }
        val etDia = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Diastólica (ej: 80)"
        }
        
        layout.addView(etSys)
        layout.addView(etDia)

        AlertDialog.Builder(requireContext())
            .setTitle("Registrar Presión Arterial")
            .setView(layout)
            .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val sys = etSys.text.toString().toFloatOrNull()
                val dia = etDia.text.toString().toFloatOrNull()
                if (sys != null && dia != null) {
                    viewModel.addRecord("BLOOD_PRESSURE", sys, dia)
                    Toast.makeText(context, R.string.msg_record_added, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun sharePdf(file: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.label_share_via)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
