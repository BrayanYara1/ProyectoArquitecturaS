package com.example.gestionturnosapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.databinding.FragmentVerifyAccountBinding
import com.example.gestionturnosapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyAccountFragment : Fragment() {

    private var _binding: FragmentVerifyAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email") ?: ""

        setupObservers()

        binding.btnVerify.setOnClickListener {
            val code = binding.etVerifyCode.text.toString().trim()
            if (code.length == 6) {
                viewModel.verify(email, code)
            } else {
                Toast.makeText(requireContext(), R.string.hint_verification_code, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResend.setOnClickListener {
            viewModel.resendCode(email)
            Toast.makeText(requireContext(), R.string.msg_code_resent, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.authState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnVerify.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), R.string.msg_verify_success, Toast.LENGTH_SHORT).show()
                    viewModel.resetAuthState()
                    if (isAdded && findNavController().currentDestination?.id == R.id.verifyAccountFragment) {
                        findNavController().navigate(R.id.action_verifyAccountFragment_to_loginFragment, null, 
                            androidx.navigation.NavOptions.Builder().setPopUpTo(R.id.verifyAccountFragment, true).build())
                    }
                }
                is Resource.Error -> {
                    binding.btnVerify.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.btnVerify.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
