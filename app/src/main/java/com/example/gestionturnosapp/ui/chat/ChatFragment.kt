package com.example.gestionturnosapp.ui.chat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    private var tts: TextToSpeech? = null
    private var isTtsEnabled = false

    @Inject lateinit var userManager: UserManager

    private val speechLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)?.let {
                binding.etMessage.setText(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Manejar insets para diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            
            binding.toolbar.updatePadding(top = systemBars.top)
            binding.cardInput.setPadding(0, 0, 0, if (ime.bottom > 0) 0 else systemBars.bottom)
            
            // Ajustar el bottom margin de cardInput para subir con el teclado
            val layoutParams = binding.cardInput.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = if (ime.bottom > 0) ime.bottom else systemBars.bottom + 16 // 16dp de margen base
            binding.cardInput.layoutParams = layoutParams
            
            insets
        }

        tts = TextToSpeech(requireContext(), this)
        setupRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        binding.rvChat.adapter = adapter
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.enviarMensaje(text)
                binding.etMessage.text?.clear()
            }
        }

        binding.btnVoice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
            try { speechLauncher.launch(intent) } catch (e: Exception) { }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val isNewMessage = state.mensajes.size > adapter.itemCount
                    adapter.submitList(state.mensajes) {
                        if (isNewMessage) binding.rvChat.smoothScrollToPosition(adapter.itemCount)
                        
                        if (isNewMessage && state.mensajes.isNotEmpty()) {
                            val last = state.mensajes.last()
                            if (last.remitente == "DOCTOR" && isTtsEnabled) speak(last.texto)
                        }
                    }
                    
                    binding.progressBar.isVisible = state.isLoading && state.mensajes.isEmpty()
                    binding.layoutEmptyChat.isVisible = !state.isLoading && state.mensajes.isEmpty()
                    binding.tvTypingIndicator.isVisible = state.isDoctorTyping
                    
                    state.errorMessage?.let {
                        if (it.contains("401")) handleSessionExpired()
                        else Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts?.setLanguage(Locale("es", "ES")) != TextToSpeech.LANG_NOT_SUPPORTED) isTtsEnabled = true
        }
    }

    private fun speak(text: String) { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) }

    private fun handleSessionExpired() {
        userManager.logout()
        findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true)
            .build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts?.shutdown()
        _binding = null
    }
}
