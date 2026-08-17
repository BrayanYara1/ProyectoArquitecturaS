package com.example.gestionturnosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        applyEntranceAnimations()

        try {
            val user = userManager.getUser()
            if (user != null) {
                // Postear la navegación para asegurar que el fragmento está listo
                view.post {
                    if (isAdded && findNavController().currentDestination?.id == R.id.welcomeFragment) {
                        findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
                    }
                }
                return
            }

            binding.btnLoginWelcome.setOnClickListener {
                if (isAdded && findNavController().currentDestination?.id == R.id.welcomeFragment) {
                    it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                    findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
                }
            }
            binding.btnRegisterWelcome.setOnClickListener {
                if (isAdded && findNavController().currentDestination?.id == R.id.welcomeFragment) {
                    it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                    findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
                }
            }
        } catch (t: Throwable) {
            android.util.Log.e("WelcomeFragment", "Error en onViewCreated", t)
        }
    }

    private fun applyEntranceAnimations() {
        binding.logoContainer.alpha = 0f
        binding.logoContainer.scaleX = 0.5f
        binding.logoContainer.scaleY = 0.5f
        binding.tvWelcomeTitle.alpha = 0f
        binding.tvWelcomeSubtitle.alpha = 0f
        binding.layoutAuthButtons.alpha = 0f
        binding.layoutAuthButtons.translationY = 50f

        binding.logoContainer.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(800).setInterpolator(android.view.animation.OvershootInterpolator()).start()
        binding.tvWelcomeTitle.animate().alpha(1f).setDuration(600).setStartDelay(300).start()
        binding.tvWelcomeSubtitle.animate().alpha(1f).setDuration(600).setStartDelay(500).start()
        binding.layoutAuthButtons.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(700).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}