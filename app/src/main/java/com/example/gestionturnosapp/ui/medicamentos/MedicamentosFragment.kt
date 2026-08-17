package com.example.gestionturnosapp.ui.medicamentos

import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.databinding.FragmentMedicamentosBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.io.File
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MedicamentosFragment : Fragment() {

    private var _binding: FragmentMedicamentosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedicamentosViewModel by viewModels()
    private lateinit var adapter: MedicamentosAdapter

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            val file = File(requireContext().cacheDir, "temp_med_scan.jpg")
            val out = java.io.FileOutputStream(file)
            it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
            out.flush(); out.close()
            analyzeLabel(android.net.Uri.fromFile(file))
        }
    }

    private val scanLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { analyzeLabel(it) }
    }

    @javax.inject.Inject lateinit var reminderManager: com.example.gestionturnosapp.notifications.ReminderManager
    @javax.inject.Inject lateinit var userManager: com.example.gestionturnosapp.data.UserManager
    @javax.inject.Inject lateinit var offlineCacheManager: com.example.gestionturnosapp.data.local.OfflineCacheManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMedicamentosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updatePadding(top = systemBars.top)
            binding.root.updatePadding(bottom = systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
        observeUiState()
        
        binding.layoutEmpty.apply {
            tvEmptyTitle.text = getString(R.string.title_no_meds)
            tvEmptyMessage.text = getString(R.string.msg_no_medication)
            ivEmptyIcon.setImageResource(R.drawable.ic_medical_logo)
        }
    }

    private fun setupRecyclerView() {
        adapter = MedicamentosAdapter(
            onItemClick = { showMedicationDetailDialog(it) },
            onDeleteClick = { med ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.title_delete_medication)
                    .setMessage(getString(R.string.msg_confirm_delete_medication, med.nombre))
                    .setPositiveButton(R.string.btn_delete_confirm) { _, _ -> viewModel.eliminarMedicamento(med.id) }
                    .setNegativeButton(R.string.btn_cancel_dialog, null)
                    .show()
            },
            onTakeClick = { med ->
                viewModel.marcarComoTomado(med)
            }
        )
        binding.rvMedicamentos.layoutManager = LinearLayoutManager(context)
        binding.rvMedicamentos.adapter = adapter
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.btnScanMed.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.title_scan_medication)
                .setPositiveButton(R.string.btn_camera) { _, _ -> cameraLauncher.launch(null) }
                .setNeutralButton(R.string.btn_gallery) { _, _ -> scanLauncher.launch("image/*") }
                .show()
        }

        binding.btnSaveMed.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            val name = binding.etMedName.text.toString()
            val dose = binding.etMedDose.text.toString()
            val freq = binding.etMedFreq.text.toString()
            val next = binding.etMedNext.text.toString()
            val stock = binding.etMedStock.text.toString().toIntOrNull() ?: 30
            val minStock = binding.etMedMinStock.text.toString().toIntOrNull() ?: 5

            if (name.isBlank() || dose.isBlank()) {
                Snackbar.make(binding.root, R.string.msg_complete_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.agregarMedicamento(name, dose, freq, next, stock, minStock)
        }

        binding.etMedNext.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance()[Calendar.HOUR_OF_DAY])
                .setMinute(Calendar.getInstance()[Calendar.MINUTE])
                .setTitleText(R.string.hint_med_next)
                .build()

            picker.addOnPositiveButtonClickListener {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, picker.hour)
                    set(Calendar.MINUTE, picker.minute)
                }
                binding.etMedNext.setText(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time))
            }
            picker.show(childFragmentManager, "TIME_PICKER")
        }

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.medicamentos)
                    binding.progressBar.isVisible = state.isLoading
                    binding.layoutEmpty.root.isVisible = !state.isLoading && state.medicamentos.isEmpty()
                    binding.btnSaveMed.isEnabled = !state.isLoading

                    if (state.isOperationSuccessful) {
                        Toast.makeText(context, R.string.msg_medication_saved, Toast.LENGTH_SHORT).show()
                        clearFields()
                        viewModel.resetOperationState()
                    }

                    state.errorMessage?.let {
                        if (it.contains("401")) handleSessionExpired()
                        else Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun analyzeLabel(uri: android.net.Uri) {
        com.example.gestionturnosapp.util.MedicineScanner.scanLabel(requireContext(), uri) { name, dose ->
            name?.let {
                binding.etMedName.setText(it)
                binding.etMedDose.setText(dose)
            }
        }
    }

    private fun showMedicationDetailDialog(med: Medicamento) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(med.nombre)
            .setMessage("${getString(R.string.hint_med_dose)}: ${med.dosis}\n${getString(R.string.hint_med_freq)}: ${med.frecuencia}")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun clearFields() {
        binding.etMedName.text?.clear()
        binding.etMedDose.text?.clear()
        binding.etMedFreq.text?.clear()
        binding.etMedNext.text?.clear()
    }

    private fun handleSessionExpired() {
        viewLifecycleOwner.lifecycleScope.launch {
            try { offlineCacheManager.clearCache() } catch (_: Exception) {}
            userManager.logout()
            findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
