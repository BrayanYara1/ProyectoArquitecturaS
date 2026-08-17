package com.example.gestionturnosapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.local.PreferenceManager
import com.example.gestionturnosapp.databinding.FragmentSettingsBinding
import com.example.gestionturnosapp.util.BiometricHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    @Inject lateinit var preferenceManager: PreferenceManager
    @Inject lateinit var userManager: UserManager
    @Inject lateinit var offlineCacheManager: OfflineCacheManager
    @Inject lateinit var reminderManager: com.example.gestionturnosapp.notifications.ReminderManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            binding.root.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        setupListeners()
        observeUiState()
        
        binding.layoutBiometric.isVisible = BiometricHelper.isBiometricAvailable(requireContext())
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked -> viewModel.toggleDarkMode(isChecked) }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked -> viewModel.toggleBiometric(isChecked) }
        
        binding.switchHydration.setOnCheckedChangeListener { _, isChecked -> 
            viewModel.toggleHydration(isChecked)
            if (isChecked) {
                reminderManager.scheduleHydrationReminder()
                Toast.makeText(context, R.string.msg_hydration_on, Toast.LENGTH_SHORT).show()
            } else {
                reminderManager.cancelHydrationReminder()
                Toast.makeText(context, R.string.msg_hydration_off, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.btn_logout)
                .setMessage(R.string.msg_logout_confirm)
                .setPositiveButton(R.string.btn_yes) { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        offlineCacheManager.clearCache()
                        userManager.logout()
                        findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true)
                            .build())
                    }
                }
                .setNegativeButton(R.string.btn_cancel, null)
                .show()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.switchDarkMode.isChecked = state.isDarkMode
                    binding.switchBiometric.isChecked = state.isBiometricEnabled
                    binding.switchHydration.isChecked = state.isHydrationEnabled
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
