package com.example.gestionturnosapp.ui.especialidades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.databinding.FragmentEspecialidadesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EspecialidadesFragment : Fragment() {

    private var _binding: FragmentEspecialidadesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EspecialidadesViewModel by viewModels()
    private lateinit var adapter: EspecialidadesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEspecialidadesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.toolbarEspecialidades.setPadding(0, systemBars.top, 0, 0)
            binding.rvEspecialidades.setPadding(
                binding.rvEspecialidades.paddingLeft,
                binding.rvEspecialidades.paddingTop,
                binding.rvEspecialidades.paddingRight,
                systemBars.bottom
            )
            insets
        }

        setupRecyclerView()
        setupSearchView()
        observeUiState()
        
        binding.toolbarEspecialidades.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        adapter = EspecialidadesAdapter { especialidad ->
            val bundle = Bundle().apply { putString("especialidadNombre", getString(especialidad.nombreRes)) }
            findNavController().navigate(R.id.action_especialidadesFragment_to_solicitarTurnoFragment, bundle)
        }
        binding.rvEspecialidades.layoutManager = GridLayoutManager(context, 2)
        binding.rvEspecialidades.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchViewEspecialidades.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean = false
            override fun onQueryTextChange(q: String?): Boolean { viewModel.setSearchQuery(q ?: ""); return true }
        })
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.filteredEspecialidades)
                    binding.progressBar.isVisible = state.isLoading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
