package com.example.gestionturnosapp.ui.turnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.databinding.FragmentTurnosListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TurnosListFragment : Fragment() {

    private var _binding: FragmentTurnosListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TurnosListViewModel by activityViewModels()
    private lateinit var adapter: TurnosAdapter

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var offlineCacheManager: com.example.gestionturnosapp.data.local.OfflineCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough().apply { duration = 300 }
        reenterTransition = MaterialFadeThrough().apply { duration = 300 }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTurnosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updatePadding(top = systemBars.top)
            insets
        }

        setupRecyclerView()
        setupListeners()
        observeUiState()
        
        binding.layoutEmpty.apply {
            tvEmptyTitle.text = getString(R.string.title_no_appointments)
            tvEmptyMessage.text = getString(R.string.msg_no_appointments_desc)
            ivEmptyIcon.setImageResource(R.drawable.ic_nav_calendar)
            btnEmptyAction.isVisible = true
            btnEmptyAction.text = getString(R.string.btn_schedule_appointment)
            btnEmptyAction.setOnClickListener { 
                if (isAdded && findNavController().currentDestination?.id == R.id.turnosListFragment) {
                    findNavController().navigate(R.id.action_turnosListFragment_to_solicitarTurnoFragment)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = TurnosAdapter(
            onTurnoClick = { turno, itemBinding ->
                try {
                    if (isAdded && findNavController().currentDestination?.id == R.id.turnosListFragment) {
                        viewModel.resetState()
                        val bundle = Bundle().apply {
                            putString("TURNO_ID", turno.id)
                            putString("PACIENTE_NOMBRE", turno.pacienteNombre)
                            putString("TURNO_FECHA", turno.fecha)
                            putString("TURNO_HORA", turno.hora)
                            putString("TURNO_MOTIVO", turno.motivo)
                            putString("TURNO_ESTADO", turno.estado)
                            putString("TURNO_ESPECIALIDAD", turno.especialidad)
                            putString("TURNO_DOCTOR", turno.doctor)
                        }
                        
                        val extras = FragmentNavigatorExtras(
                            itemBinding.root to "card_${turno.id}",
                            itemBinding.tvItemNombre to "name_${turno.id}",
                            itemBinding.dateContainer to "date_${turno.id}"
                        )
                        
                        findNavController().navigate(
                            R.id.action_turnosListFragment_to_turnoDetailFragment,
                            bundle,
                            null,
                            extras
                        )
                    }
                } catch (e: Exception) {
                    android.util.Log.e("TurnosList", "Navigation error", e)
                    Toast.makeText(requireContext(), "Error al abrir detalle", Toast.LENGTH_SHORT).show()
                }
            },
            onDeleteClick = { turno ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.title_delete_appointment)
                    .setMessage(getString(R.string.msg_delete_confirm_with_name, turno.pacienteNombre))
                    .setPositiveButton(R.string.btn_delete) { _, _ -> viewModel.eliminarTurno(turno.id) }
                    .setNegativeButton(R.string.btn_cancel, null)
                    .show()
            }
        )
        binding.rvTurnos.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAddTurno.setOnClickListener { 
            if (isAdded && findNavController().currentDestination?.id == R.id.turnosListFragment) {
                it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                findNavController().navigate(R.id.action_turnosListFragment_to_solicitarTurnoFragment)
            }
        }
        binding.swipeRefresh.setOnRefreshListener { 
            binding.root.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
            viewModel.fetchTurnos() 
        }

        binding.layoutEmpty.btnEmptyAction.setOnClickListener {
            if (isAdded && findNavController().currentDestination?.id == R.id.turnosListFragment) {
                findNavController().navigate(R.id.action_turnosListFragment_to_solicitarTurnoFragment)
            }
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean = false
            override fun onQueryTextChange(q: String?): Boolean { 
                viewModel.setSearchQuery(q ?: "")
                return true 
            }
        })

        binding.chipGroupFilters.setOnCheckedStateChangeListener { _, ids ->
            binding.root.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            val status = when (ids.firstOrNull()) {
                R.id.chipPending -> "PENDIENTE"
                R.id.chipCompleted -> "COMPLETADO"
                R.id.chipCancelled -> "CANCELADO"
                else -> "TODOS"
            }
            viewModel.setFilterStatus(status)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.filteredTurnos)
                    binding.layoutEmpty.root.isVisible = !state.isLoading && state.filteredTurnos.isEmpty()
                    binding.swipeRefresh.isRefreshing = false
                    binding.shimmerViewContainer.isVisible = state.isLoading
                    
                    if (state.isDeleteSuccessful) {
                        Snackbar.make(binding.root, R.string.msg_cancel_success, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_schedule_appointment) {
                                findNavController().navigate(R.id.action_turnosListFragment_to_solicitarTurnoFragment)
                            }
                            .show()
                        viewModel.resetState()
                    }

                    state.errorMessage?.let {
                        if (it.contains("401")) handleSessionExpired()
                        else Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                        viewModel.resetState()
                    }
                }
            }
        }
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
