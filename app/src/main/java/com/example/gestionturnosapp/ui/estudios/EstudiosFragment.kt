package com.example.gestionturnosapp.ui.estudios

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.ImageStorageManager
import com.example.gestionturnosapp.data.local.PreferenceManager
import com.example.gestionturnosapp.data.model.EstudioMedico
import com.example.gestionturnosapp.databinding.FragmentEstudiosBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class EstudiosFragment : Fragment() {

    private var _binding: FragmentEstudiosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EstudiosViewModel by viewModels()
    private lateinit var adapter: EstudiosAdapter

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var imageStorageManager: ImageStorageManager
    @Inject lateinit var preferenceManager: PreferenceManager

    private var selectedImageUri: Uri? = null
    private var dialogPreview: ImageView? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            dialogPreview?.load(it)
            dialogPreview?.parent?.let { parent -> (parent as View).isVisible = true }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEstudiosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            binding.fabAddEstudio.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = EstudiosAdapter(
            onItemClick = { estudio ->
                if (preferenceManager.isBiometricEnabled()) {
                    com.example.gestionturnosapp.util.BiometricHelper.showBiometricPrompt(
                        this, "Bóveda Médica", "Confirma tu identidad",
                        onSuccess = { showEstudioDetailDialog(estudio) },
                        onError = { Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show() }
                    )
                } else showEstudioDetailDialog(estudio)
            },
            onDeleteClick = { estudio ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.title_delete_study)
                    .setMessage(getString(R.string.msg_confirm_delete_study, estudio.titulo))
                    .setPositiveButton(R.string.btn_delete_confirm) { _, _ -> viewModel.eliminarEstudio(estudio.id) }
                    .setNegativeButton(R.string.btn_cancel_dialog, null)
                    .show()
            }
        )
        binding.rvEstudios.layoutManager = LinearLayoutManager(context)
        binding.rvEstudios.adapter = adapter
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        
        binding.searchViewEstudios.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean = false
            override fun onQueryTextChange(q: String?): Boolean { viewModel.setSearchQuery(q ?: ""); return true }
        })

        binding.swipeRefresh.setOnRefreshListener { viewModel.loadEstudios() }
        binding.fabAddEstudio.setOnClickListener { showAddEstudioDialog() }

        binding.chipDateStart.setOnClickListener {
            showDatePicker { date ->
                viewModel.setDateFilter(date, viewModel.uiState.value.filterEnd)
                binding.chipDateStart.text = getString(R.string.filter_from_date, date)
            }
        }

        binding.chipDateEnd.setOnClickListener {
            showDatePicker { date ->
                viewModel.setDateFilter(viewModel.uiState.value.filterStart, date)
                binding.chipDateEnd.text = getString(R.string.filter_to_date, date)
            }
        }

        binding.btnClearFilters.setOnClickListener {
            viewModel.setDateFilter(null, null)
            binding.chipDateStart.text = "Desde"
            binding.chipDateEnd.text = "Hasta"
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.filteredEstudios)
                    binding.progressBar.isVisible = state.isLoading
                    binding.swipeRefresh.isRefreshing = false
                    binding.layoutEmpty.isVisible = !state.isLoading && state.filteredEstudios.isEmpty()
                    binding.btnClearFilters.isVisible = state.filterStart != null || state.filterEnd != null

                    if (state.isOperationSuccessful) {
                        Toast.makeText(context, R.string.msg_study_saved, Toast.LENGTH_SHORT).show()
                        viewModel.resetOperationState()
                    }
                }
            }
        }
    }

    private fun showAddEstudioDialog() {
        selectedImageUri = null
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_estudio, null)
        val etTitulo = dialogView.findViewById<EditText>(R.id.etEditTitulo)
        val etTipo = dialogView.findViewById<EditText>(R.id.etEditTipo)
        val etRes = dialogView.findViewById<EditText>(R.id.etEditResultado)
        val etFecha = dialogView.findViewById<EditText>(R.id.etEditFecha)
        dialogPreview = dialogView.findViewById(R.id.ivPreview)

        etFecha.setOnClickListener { showDatePicker { etFecha.setText(it) } }
        dialogView.findViewById<View>(R.id.btnPickPhoto).setOnClickListener { pickImageLauncher.launch("image/*") }

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                if (titulo.isEmpty()) return@setPositiveButton
                val path = selectedImageUri?.let { imageStorageManager.saveStudyImage(it) }
                viewModel.agregarEstudio(titulo, etFecha.text.toString(), etTipo.text.toString(), etRes.text.toString(), path)
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    private fun showEstudioDetailDialog(estudio: EstudioMedico) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_estudio_detail, null)
        view.findViewById<TextView>(R.id.tvDetailTitulo).text = estudio.titulo
        view.findViewById<TextView>(R.id.tvDetailInfo).text = "${estudio.tipo.uppercase()} • ${estudio.fecha}"
        view.findViewById<TextView>(R.id.tvDetailResultado).text = estudio.resultadoBreve
        estudio.urlDocumento?.let { 
            val iv = view.findViewById<ImageView>(R.id.ivEstudioDetail)
            iv.isVisible = true
            iv.load(it)
        }
        MaterialAlertDialogBuilder(requireContext()).setView(view).setPositiveButton("Cerrar", null).show()
    }

    private fun showDatePicker(onDate: (String) -> Unit) {
        val c = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            // Aseguramos formato consistente y evitamos desfases
            val date = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
            onDate(date)
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
