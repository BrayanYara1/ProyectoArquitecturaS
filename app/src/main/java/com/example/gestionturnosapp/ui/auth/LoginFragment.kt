package com.example.gestionturnosapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.PreferenceManager
import com.example.gestionturnosapp.util.Resource
import com.example.gestionturnosapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var userManager: UserManager

    private var biometricPrompt: BiometricPrompt? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // En login no solemos querer padding superior porque el header es decorativo
            // pero si hay botones al fondo necesitamos padding inferior
            binding.root.updatePadding(bottom = systemBars.bottom)
            insets
        }

        try {
            setupObservers()
            setupListeners()
            
            // Ejecutar verificación de biométricos de forma más segura
            binding.root.post {
                if (isAdded && !isDetached) checkBiometricAvailability()
            }
        } catch (e: Exception) {
            android.util.Log.e("LoginFragment", "Error en onViewCreated", e)
        }
    }

    private fun checkBiometricAvailability() {
        val context = context ?: return
        val hasToken = !userManager.token.isNullOrBlank()
        val isBiometricEnabled = PreferenceManager.isBiometricEnabled(context)
        
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS && hasToken && isBiometricEnabled) {
            binding.btnBiometricLogin.visibility = View.VISIBLE
            // Pequeño delay para que la transición de fragmentos termine antes del prompt
            binding.root.postDelayed({
                if (isAdded && !isRemoving && !isDetached) {
                    showBiometricPrompt()
                }
            }, 600)
        } else {
            binding.btnBiometricLogin.visibility = View.GONE
        }
    }

    private fun showBiometricPrompt() {
        if (!isAdded) return
        
        val executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // El usuario puede loguearse manualmente si falla
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    if (isAdded && !isRemoving) {
                        val controller = findNavController()
                        if (controller.currentDestination?.id == R.id.loginFragment) {
                            controller.navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.title_biometric_login))
            .setSubtitle(getString(R.string.subtitle_biometric_login))
            .setNegativeButtonText(getString(R.string.btn_use_password))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        try {
            biometricPrompt?.authenticate(promptInfo)
        } catch (e: Exception) {
            android.util.Log.e("LoginFragment", "Biometric authentication failed to start", e)
        }
    }

    private fun setupListeners() {
        // Inicializar campos con valores del ViewModel para evitar pérdida de datos en recreación
        binding.etEmail.setText(viewModel.loginEmail.value)
        binding.etPassword.setText(viewModel.loginPassword.value)

        binding.btnBiometricLogin.setOnClickListener {
            showBiometricPrompt()
        }
        // Enlace bidireccional manual (sin DataBinding para mayor control)
        binding.etEmail.doAfterTextChanged { 
            val email = it.toString()
            viewModel.loginEmail.value = email
            if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = getString(R.string.msg_invalid_email)
            } else {
                binding.tilEmail.error = null 
            }
        }
        binding.etPassword.doAfterTextChanged { 
            val pass = it.toString()
            viewModel.loginPassword.value = pass
            if (pass.isNotEmpty() && pass.length < 6) {
                binding.tilPassword.error = getString(R.string.msg_password_length)
            } else {
                binding.tilPassword.error = null 
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupObservers() {
        viewModel.isLoginValid.observe(viewLifecycleOwner) { isValid ->
            binding.btnLogin.isEnabled = isValid
            // Opción: Cambiar opacidad del botón si no es válido
            binding.btnLogin.alpha = if (isValid) 1.0f else 0.5f
        }

        viewModel.authState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success<*> -> {
                    binding.progressBar.isVisible = false
                    binding.btnLogin.isEnabled = true
                    viewModel.resetAuthState() // Limpiar estado para evitar re-navegación
                    
                    if (isAdded && !isRemoving) {
                        val controller = findNavController()
                        if (controller.currentDestination?.id == R.id.loginFragment) {
                            controller.navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.isVisible = false
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            biometricPrompt?.cancelAuthentication()
        } catch (_: Exception) {}
        _binding = null
    }
}
