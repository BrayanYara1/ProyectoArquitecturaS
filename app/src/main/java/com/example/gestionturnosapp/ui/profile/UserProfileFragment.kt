package com.example.gestionturnosapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.ImageStorageManager
import com.example.gestionturnosapp.data.model.Usuario
import com.example.gestionturnosapp.databinding.FragmentUserProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var imageStorageManager: ImageStorageManager
    @Inject lateinit var offlineCacheManager: com.example.gestionturnosapp.data.local.OfflineCacheManager

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val userId = userManager.usuarioActual?.id ?: "unknown"
            imageStorageManager.saveProfileImage(userId, it)?.let { _ ->
                updateAvatar()
                Toast.makeText(requireContext(), R.string.photo_updated, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.profileToolbar.updatePadding(top = systemBars.top)
            binding.root.updatePadding(bottom = systemBars.bottom)
            insets
        }

        setupListeners()
        observeUiState()
        updateAvatar()
    }

    private fun setupListeners() {
        binding.profileToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        
        binding.ivProfileAvatar.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            pickImageLauncher.launch("image/*")
        }

        binding.btnEditProfile.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            mostrarDialogoEdicion()
        }

        binding.btnProfileSettings.setOnClickListener { findNavController().navigate(R.id.action_userProfileFragment_to_settingsFragment) }
        binding.btnHealthStats.setOnClickListener { findNavController().navigate(R.id.action_userProfileFragment_to_healthStatsFragment) }
        binding.btnAchievements.setOnClickListener { findNavController().navigate(R.id.action_userProfileFragment_to_achievementsFragment) }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.user?.let {
                        binding.tvProfileName.text = it.nombre
                        binding.tvProfileEmail.text = it.email
                        binding.tvProfilePhone.text = it.telefono ?: getString(R.string.label_not_specified)
                    }
                    
                    binding.btnEditProfile.isEnabled = !state.isLoading
                    
                    if (state.isUpdateSuccessful) {
                        Toast.makeText(context, R.string.msg_profile_update_success, Toast.LENGTH_SHORT).show()
                        viewModel.resetUpdateStatus()
                    }

                    state.errorMessage?.let {
                        when (it) {
                            "SESSION_EXPIRED" -> handleSessionExpired()
                            "OFFLINE_SAVED" -> Toast.makeText(context, R.string.msg_profile_sync_local, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                        viewModel.resetUpdateStatus()
                    }
                }
            }
        }
    }

    private fun updateAvatar() {
        binding.ivProfileAvatar.load(imageStorageManager.getProfileImageUri()) {
            crossfade(true)
            placeholder(R.drawable.ic_nav_profile)
            transformations(CircleCropTransformation())
        }
    }

    private fun mostrarDialogoEdicion() {
        val user = viewModel.uiState.value.user
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
        
        val etName = view.findViewById<EditText>(R.id.etEditName).apply { setText(user?.nombre) }
        val etPhone = view.findViewById<EditText>(R.id.etEditPhone).apply { setText(user?.telefono) }
        val etBlood = view.findViewById<EditText>(R.id.etEditBloodType).apply { setText(user?.tipoSanguineo) }
        val etAllergies = view.findViewById<EditText>(R.id.etEditAllergies).apply { setText(user?.alergias) }
        val etConditions = view.findViewById<EditText>(R.id.etEditConditions).apply { setText(user?.condiciones) }
        val etEmergency = view.findViewById<EditText>(R.id.etEditEmergencyContact).apply { setText(user?.contactoEmergencia) }

        MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val updatedUser = user?.copy(
                    nombre = etName.text.toString().trim(),
                    telefono = etPhone.text.toString().trim(),
                    tipoSanguineo = etBlood.text.toString().trim(),
                    alergias = etAllergies.text.toString().trim(),
                    condiciones = etConditions.text.toString().trim(),
                    contactoEmergencia = etEmergency.text.toString().trim()
                ) ?: return@setPositiveButton
                if (updatedUser.nombre.isNotEmpty()) viewModel.updateProfile(updatedUser)
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
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
