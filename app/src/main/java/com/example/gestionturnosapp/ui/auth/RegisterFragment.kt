package com.example.gestionturnosapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.remote.dto.RegisterRequest
import com.example.gestionturnosapp.util.Resource
import com.example.gestionturnosapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.updatePadding(bottom = systemBars.bottom)
            insets
        }

        setupObservers()
        setupRegisterActions()
    }

    private fun setupObservers() {
        viewModel.isRegisterValid.observe(viewLifecycleOwner) { isValid ->
            binding.btnRegister.isEnabled = isValid
            binding.btnRegister.alpha = if (isValid) 1.0f else 0.5f
        }

        viewModel.authState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    binding.btnRegister.isEnabled = true
                    
                    // Navegar a verificación
                    if (isAdded && findNavController().currentDestination?.id == R.id.registerFragment) {
                        val bundle = Bundle().apply { putString("email", viewModel.regEmail.value) }
                        findNavController().navigate(R.id.action_registerFragment_to_verifyAccountFragment, bundle)
                        viewModel.resetAuthState()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    binding.btnRegister.isEnabled = true
                    
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.isVisible = false
                    binding.btnRegister.isEnabled = true
                }
            }
        }
    }

    private fun setupRegisterActions() {
        // Inicializar campos con valores del ViewModel
        binding.etNombre.setText(viewModel.regName.value)
        binding.etEmail.setText(viewModel.regEmail.value)
        binding.etPassword.setText(viewModel.regPassword.value)
        binding.etTelefono.setText(viewModel.regPhone.value)

        // Enlace bidireccional manual
        binding.etNombre.doAfterTextChanged { 
            viewModel.regName.value = it.toString()
            binding.tilNombre.error = null 
        }
        binding.etEmail.doAfterTextChanged { 
            val email = it.toString()
            viewModel.regEmail.value = email
            if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = getString(R.string.msg_invalid_email)
            } else {
                binding.tilEmail.error = null 
            }
        }
        binding.etPassword.doAfterTextChanged { 
            val pass = it.toString()
            viewModel.regPassword.value = pass
            if (pass.isNotEmpty() && pass.length < 6) {
                binding.tilPassword.error = getString(R.string.msg_password_length)
            } else {
                binding.tilPassword.error = null 
            }
        }

        binding.etTelefono.addTextChangedListener(object : android.text.TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                if (isUpdating) return
                
                val text = s.toString()
                // Solo formatear si tiene al menos un dígito y no tiene el prefijo
                if (text.isNotEmpty() && !text.startsWith("+57 ")) {
                    isUpdating = true
                    val digitsOnly = text.filter { it.isDigit() }
                    val newText = if (digitsOnly.startsWith("57") && digitsOnly.length > 2) {
                        "+57 ${digitsOnly.substring(2)}"
                    } else {
                        "+57 $digitsOnly"
                    }
                    binding.etTelefono.setText(newText)
                    binding.etTelefono.setSelection(binding.etTelefono.length())
                    isUpdating = false
                }
                viewModel.regPhone.value = binding.etTelefono.text.toString()
                binding.tilTelefono.error = null
            }
        })

        binding.btnRegister.setOnClickListener {
            viewModel.register()
        }

        binding.tvGoToLogin.setOnClickListener {
            if (isAdded && findNavController().currentDestination?.id == R.id.registerFragment) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
