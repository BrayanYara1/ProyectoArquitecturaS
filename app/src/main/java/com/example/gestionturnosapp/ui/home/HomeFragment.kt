package com.example.gestionturnosapp.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import java.util.Calendar
import coil.load
import coil.transform.CircleCropTransformation
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.ImageStorageManager
import com.example.gestionturnosapp.databinding.FragmentHomeBinding
import com.example.gestionturnosapp.util.DateUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), android.speech.tts.TextToSpeech.OnInitListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var imageStorageManager: ImageStorageManager
    @Inject lateinit var offlineCacheManager: com.example.gestionturnosapp.data.local.OfflineCacheManager
    @Inject lateinit var smartAssistant: com.example.gestionturnosapp.util.SmartAssistant
    @Inject lateinit var emergencyManager: com.example.gestionturnosapp.util.EmergencyManager

    private var tts: android.speech.tts.TextToSpeech? = null
    private var isTtsReady = false

    private val emergencyPermissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            triggerEmergency()
        } else {
            Toast.makeText(requireContext(), "Se requieren permisos para enviar la alerta", Toast.LENGTH_SHORT).show()
        }
    }

    private val voiceLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let { processVoiceCommand(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            // Aplicar Insets de forma segura para diseño Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                binding.topBar.updatePadding(top = systemBars.top)
                binding.root.updatePadding(bottom = systemBars.bottom)
                insets
            }
            
            // TTS diferido
            binding.root.postDelayed({
                if (_binding != null && isAdded) {
                    try {
                        tts = android.speech.tts.TextToSpeech(requireContext(), this)
                    } catch (_: Exception) {}
                }
            }, 1000)

            setupUI()
            observeUiState()
            applyEntranceAnimations()
        } catch (e: Exception) {
            android.util.Log.e("HomeFragment", "Error fatal en inicio de Home", e)
        }
    }

    private fun setupUI() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshData() }

        val navigateTo = { actionId: Int, view: View, bundle: Bundle? ->
            if (isAdded && findNavController().currentDestination?.id == R.id.homeFragment) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                findNavController().navigate(actionId, bundle)
            }
        }

        binding.cardSolicitarTurno.setOnClickListener { navigateTo(R.id.action_homeFragment_to_solicitarTurnoFragment, it, null) }
        binding.cardMisTurnos.setOnClickListener { navigateTo(R.id.action_homeFragment_to_turnosListFragment, it, null) }
        binding.cardMedication.setOnClickListener { navigateTo(R.id.action_homeFragment_to_medicamentosFragment, it, null) }
        binding.btnAddMedHome.setOnClickListener { navigateTo(R.id.action_homeFragment_to_medicamentosFragment, it, null) }
        
        binding.cardHealthStatsHome.setOnClickListener { navigateTo(R.id.action_homeFragment_to_healthStatsFragment, it, null) }
        binding.btnQuickSymptom.setOnClickListener { navigateTo(R.id.action_homeFragment_to_healthStatsFragment, it, null) }

        binding.cardHomeProfile.setOnClickListener { 
            if (isAdded && findNavController().currentDestination?.id == R.id.homeFragment) {
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                val extras = androidx.navigation.fragment.FragmentNavigatorExtras(binding.cardHomeProfile to "profile_avatar_transition")
                findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment, null, null, extras)
            }
        }

        binding.fabVoiceAssistant.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            startVoiceRecognition()
        }

        binding.fabEmergency.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            checkEmergencyPermissions()
        }
    }

    private fun checkEmergencyPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.CALL_PHONE
        )
        if (permissions.all { androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), it) == android.content.pm.PackageManager.PERMISSION_GRANTED }) {
            showEmergencyConfirmDialog()
        } else {
            emergencyPermissionLauncher.launch(permissions)
        }
    }

    private fun showEmergencyConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.msg_emergency_mode_title)
            .setMessage(R.string.msg_emergency_mode_body)
            .setPositiveButton(R.string.btn_send_alert) { _, _ -> triggerEmergency() }
            .setNeutralButton(R.string.btn_only_call) { _, _ -> makeEmergencyCall() }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    private fun triggerEmergency() {
        emergencyManager.sendEmergencyAlert { success, msg ->
            if (isAdded) Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            if (success) makeEmergencyCall()
        }
    }

    private fun makeEmergencyCall() {
        val intent = Intent(Intent.ACTION_CALL, "tel:${getString(R.string.emergency_phone_number)}".toUri())
        try { startActivity(intent) } catch (_: Exception) { }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!isAdded || isRemoving) return@collect
                    
                    updateHeader(state)
                    updateNextAppointment(state)
                    updateHealthStats(state)
                    updateMedication(state)
                    
                    if (state.healthTipResId != null && state.healthTipResId != 0) {
                        binding.tvHealthTip.setText(state.healthTipResId)
                    }
                    // Desactivar Shimmer por estabilidad
                    binding.shimmerHome.visibility = View.GONE
                    binding.shimmerHome.stopShimmer()
                    
                    state.errorMessage?.let {
                        if (it == "SESSION_EXPIRED") {
                            handleSessionExpired()
                        } else {
                            if (isAdded) Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateHeader(state: HomeUiState) {
        try {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            
            val greeting = when (hour) {
                in 6..12 -> getString(R.string.greeting_morning, state.userName)
                in 13..19 -> getString(R.string.greeting_afternoon, state.userName)
                else -> getString(R.string.greeting_evening, state.userName)
            }
            
            binding.tvGreeting.text = greeting
            binding.tvCurrentDate.text = com.example.gestionturnosapp.util.DateUtils.getTodayLongDisplay()
            binding.tvHealthScore.text = getString(R.string.label_health_score_short, state.healthScore)
            
            // Animación suave del progreso
            val animator = android.animation.ValueAnimator.ofInt(binding.cpHealthScore.progress, state.healthScore)
            animator.duration = 800
            animator.addUpdateListener { 
                if (_binding != null) {
                    binding.cpHealthScore.progress = it.animatedValue as Int
                }
            }
            animator.start()
            
            binding.tvHealthStreak.text = "🔥 ${state.healthStreak} días"
            binding.tvHealthStreak.visibility = if (state.healthStreak > 0) View.VISIBLE else View.GONE
            
            try {
                val avatar = imageStorageManager.getProfileImageUri()
                binding.ivUserAvatar.load(avatar) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_nav_profile)
                    error(R.drawable.ic_nav_profile)
                }
            } catch (e: Exception) {
                binding.ivUserAvatar.setImageResource(R.drawable.ic_nav_profile)
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeFragment", "Error en updateHeader", e)
        }
    }

    private fun updateNextAppointment(state: HomeUiState) {
        // Actualizar el badge de turnos
        binding.tvBadgeTurnos.apply {
            isVisible = state.turnosCount > 0
            text = if (state.turnosCount > 9) "9+" else state.turnosCount.toString()
        }

        val next = state.nextTurno
        if (next != null) {
            try {
                binding.tvNextAppointName.text = next.especialidad ?: "Consulta General"
                binding.tvNextAppointDoctor.text = next.doctor ?: "Dr. Asignado"
                
                binding.tvNextAppointDate.text = com.example.gestionturnosapp.util.DateUtils.formatDisplayTime(next.hora)
                
                // Formatear Fecha para el Badge
                try {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                    val date = sdf.parse(next.fecha)
                    if (date != null) {
                        val cal = Calendar.getInstance()
                        cal.time = date
                        binding.tvNextAppointDia.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                        binding.tvNextAppointMes.text = java.text.SimpleDateFormat("MMM", java.util.Locale.getDefault()).format(date).uppercase()
                    }
                } catch (_: Exception) {
                    binding.tvNextAppointDia.text = "??"
                    binding.tvNextAppointMes.text = "ERR"
                }

                binding.cardNextAppointment.setOnClickListener {
                    if (isAdded && findNavController().currentDestination?.id == R.id.homeFragment) {
                        val bundle = Bundle().apply {
                            putString("TURNO_ID", next.id)
                            putString("PACIENTE_NOMBRE", next.pacienteNombre)
                            putString("TURNO_FECHA", next.fecha)
                            putString("TURNO_HORA", next.hora)
                            putString("TURNO_MOTIVO", next.motivo)
                            putString("TURNO_ESTADO", next.estado)
                            putString("TURNO_ESPECIALIDAD", next.especialidad)
                            putString("TURNO_DOCTOR", next.doctor)
                        }
                        findNavController().navigate(R.id.action_homeFragment_to_turnoDetailFragment, bundle)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("HomeFragment", "Error al actualizar cita", e)
            }
        } else {
            binding.tvNextAppointName.text = getString(R.string.no_upcoming_appointments)
            binding.tvNextAppointDoctor.text = getString(R.string.menu_request_appointment)
            binding.tvNextAppointDate.text = "--:--"
            binding.tvNextAppointDia.text = "--"
            binding.tvNextAppointMes.text = "---"
            binding.cardNextAppointment.setOnClickListener {
                if (isAdded && findNavController().currentDestination?.id == R.id.homeFragment) {
                    findNavController().navigate(R.id.action_homeFragment_to_solicitarTurnoFragment)
                }
            }
        }
    }

    private fun updateHealthStats(state: HomeUiState) {
        try {
            binding.tvHomeWeight.text = state.weightRecords.lastOrNull()?.value?.toString() ?: "--"
            binding.tvHomeGlucose.text = state.glucoseRecords.lastOrNull()?.value?.toInt()?.toString() ?: "--"
            binding.tvHomePressure.text = state.pressureRecords.lastOrNull()?.let { 
                "${it.value.toInt()}/${it.valueSecondary?.toInt() ?: ""}" 
            } ?: "--"
            
            state.healthStatus?.let {
                binding.tvHealthStatus.text = it.status
                binding.tvHealthStatus.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), it.colorRes))
                binding.ivHealthStatus.setImageResource(it.iconRes)
                binding.ivHealthStatus.imageTintList = android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(requireContext(), it.colorRes))
                binding.tvHealthStatusMsg.text = it.message
            }
            
            if (state.healthInsights.isNotEmpty()) {
                binding.cardInsight.visibility = View.VISIBLE
                binding.tvHealthInsight.text = state.healthInsights
            } else {
                binding.cardInsight.visibility = View.GONE
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeFragment", "Error en updateHealthStats", e)
        }
    }

    private fun updateMedication(state: HomeUiState) {
        if (!isAdded) return
        try {
            binding.tvNoMeds.visibility = if (state.medicamentos.isEmpty()) View.VISIBLE else View.GONE
            val container = binding.layoutMedication
            
            // Eliminar solo los items dinámicos
            val viewsToRemove = mutableListOf<View>()
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child.id != R.id.tvNoMeds) viewsToRemove.add(child)
            }
            viewsToRemove.forEach { container.removeView(it) }

            state.medicamentos.take(3).forEachIndexed { index, med ->
                val medBinding = com.example.gestionturnosapp.databinding.ItemMedicationHomeBinding.inflate(
                    LayoutInflater.from(requireContext()), container, false
                )
                
                medBinding.tvMedName.text = med.nombre
                medBinding.tvMedSchedule.text = "${med.frecuencia} • Próxima: ${med.proximaToma}"
                
                medBinding.ivMedInfo.setOnClickListener {
                    it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                    // Animación de check
                    it.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction {
                        it.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                    }.start()
                    viewModel.marcarComoTomado(med)
                }
                
                // Animación de entrada escalonada
                medBinding.root.alpha = 0f
                medBinding.root.translationX = 50f
                container.addView(medBinding.root)
                medBinding.root.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(400)
                    .setStartDelay(index * 100L)
                    .start()
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeFragment", "Error en updateMedication", e)
        }
    }

    private fun handleSessionExpired() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Limpiar cache local para evitar que el siguiente usuario vea datos viejos
                offlineCacheManager.clearCache()
                userManager.logout()
                findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build())
            } catch (_: Exception) {
                userManager.logout()
                findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build())
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
        }
        try { voiceLauncher.launch(intent) } catch (e: Exception) { }
    }

    private fun processVoiceCommand(command: String) {
        val state = viewModel.uiState.value
        val response = smartAssistant.generateResponse(
            query = command,
            turnos = state.allTurnos,
            meds = state.medicamentos,
            records = state.weightRecords + state.glucoseRecords + state.pressureRecords
        )
        
        speak(response)
        
        if (!isAdded || findNavController().currentDestination?.id != R.id.homeFragment) return

        val cmd = command.lowercase()
        // Navegación inteligente basada en palabras clave
        when {
            cmd.contains("turno") || cmd.contains("cita") || cmd.contains("agenda") -> {
                findNavController().navigate(R.id.action_homeFragment_to_turnosListFragment)
            }
            cmd.contains("perfil") || cmd.contains("mi cuenta") -> {
                findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
            }
            cmd.contains("salud") || cmd.contains("estadística") || cmd.contains("presión") || cmd.contains("peso") || cmd.contains("glucosa") -> {
                findNavController().navigate(R.id.action_homeFragment_to_healthStatsFragment)
            }
            cmd.contains("medica") || cmd.contains("pastilla") || cmd.contains("remedio") -> {
                findNavController().navigate(R.id.action_homeFragment_to_medicamentosFragment)
            }
            cmd.contains("estudio") || cmd.contains("examen") || cmd.contains("resultado") -> {
                findNavController().navigate(R.id.action_homeFragment_to_estudiosFragment)
            }
        }
    }

    private fun speak(text: String) {
        if (isTtsReady) {
            tts?.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    private fun applyEntranceAnimations() {
        binding.cardNextAppointment.alpha = 0f
        binding.cardNextAppointment.translationY = 100f
        binding.gridActions.alpha = 0f
        binding.gridActions.translationY = 100f
        binding.cardHealthStatsHome.alpha = 0f
        binding.cardHealthStatsHome.translationY = 100f

        binding.cardNextAppointment.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(200).start()
        binding.gridActions.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(400).start()
        binding.cardHealthStatsHome.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(600).start()
    }

    override fun onInit(status: Int) {
        if (status == android.speech.tts.TextToSpeech.SUCCESS) {
            tts?.language = java.util.Locale.getDefault()
            isTtsReady = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts?.shutdown()
        _binding = null
    }
}
