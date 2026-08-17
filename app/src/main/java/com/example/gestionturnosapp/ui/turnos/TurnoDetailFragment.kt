package com.example.gestionturnosapp.ui.turnos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.databinding.FragmentTurnoDetailBinding
import com.example.gestionturnosapp.notifications.ReminderManager
import com.example.gestionturnosapp.notifications.ReminderReceiver
import com.example.gestionturnosapp.util.DateUtils
import com.example.gestionturnosapp.data.model.Turno
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TurnoDetailFragment : Fragment() {

    private var _binding: FragmentTurnoDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TurnosListViewModel by activityViewModels()

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var reminderManager: ReminderManager
    @Inject lateinit var offlineCacheManager: com.example.gestionturnosapp.data.local.OfflineCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = com.google.android.material.transition.MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 450
            scrimColor = android.graphics.Color.TRANSPARENT
            setAllContainerColors(android.graphics.Color.TRANSPARENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTurnoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        try {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                binding.toolbar.updatePadding(top = systemBars.top)
                insets
            }

            if (savedInstanceState == null) {
                viewModel.resetState()
                
                binding.cardDetail.alpha = 0f
                binding.cardDetail.translationY = 50f
                binding.cardDetail.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setInterpolator(android.view.animation.DecelerateInterpolator())
                    .start()
            }

            val id = arguments?.getString("TURNO_ID") ?: ""
            
            // Shared Element Transition Names
            binding.cardDetail.transitionName = "card_$id"
            binding.tvDetailNombre.transitionName = "name_$id"
            binding.dateBadge.transitionName = "date_$id"

            val fromNotification = arguments?.getBoolean("FROM_NOTIFICATION") ?: false

            if (fromNotification) {
                val turno = viewModel.uiState.value.allTurnos.find { it.id == id }
                if (turno != null) {
                    setupUI(id, turno.pacienteNombre, turno.fecha, turno.hora, turno.motivo, turno.estado, turno.especialidad, turno.doctor)
                } else {
                    setupUI(id, getString(R.string.label_anonymous), "0000-00-00", "00:00", "...", "...", "...", "...")
                }
            } else {
                val paciente = arguments?.getString("PACIENTE_NOMBRE") ?: getString(R.string.label_anonymous)
                val fecha = arguments?.getString("TURNO_FECHA") ?: "0000-00-00"
                val hora = arguments?.getString("TURNO_HORA") ?: "00:00"
                val motivo = arguments?.getString("TURNO_MOTIVO") ?: getString(R.string.no_appointments)
                val estado = arguments?.getString("TURNO_ESTADO") ?: "Pendiente"
                val especialidad = arguments?.getString("TURNO_ESPECIALIDAD") ?: "General"
                val doctor = arguments?.getString("TURNO_DOCTOR") ?: "Dr. Asignado"
                setupUI(id, paciente, fecha, hora, motivo, estado, especialidad, doctor)
            }

            observeUiState()
            
            // Iniciar la transición después de que la vista esté lista
            binding.root.post { startPostponedEnterTransition() }
        } catch (e: Exception) {
            android.util.Log.e("TurnoDetail", "Fatal crash in onViewCreated", e)
        }
    }

    private fun setupUI(id: String, paciente: String, fecha: String, hora: String, motivo: String, estado: String, especialidad: String?, doctor: String?) {
        binding.apply {
            tvDetailNombre.text = paciente
            tvDetailEspecialidad.text = especialidad ?: getString(R.string.label_default_specialty)
            
            // Limpiar Doctor si es el por defecto para evitar redundancia visual
            val assignedDoctorStr = getString(R.string.label_assigned_doctor)
            tvDetailDoctor.text = if (doctor == null || doctor.contains("Asignado") || doctor.contains("Assigned")) {
                assignedDoctorStr
            } else {
                if (!doctor.startsWith("Dr.") && !doctor.startsWith("Dra.")) "Dr. $doctor" else doctor
            }
            
            try {
                if (fecha != "0000-00-00") {
                    val sdfInput = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                    val date = sdfInput.parse(fecha)
                    if (date != null) {
                        val cal = Calendar.getInstance()
                        cal.time = date
                        tvDetailDia.text = String.format(java.util.Locale.getDefault(), "%02d", cal.get(Calendar.DAY_OF_MONTH))
                        tvDetailMes.text = java.text.SimpleDateFormat("MMM", java.util.Locale.getDefault()).format(date).uppercase()
                    }

                } else {
                    tvDetailDia.text = "--"
                    tvDetailMes.text = "---"
                }
            } catch (_: Exception) {
                tvDetailDia.text = "??"
                tvDetailMes.text = "ERR"
            }

            val displayTime = try { DateUtils.formatDisplayTime(hora) } catch (_: Exception) { hora }
            val displayDate = try { DateUtils.formatDisplayDate(context, fecha) } catch (_: Exception) { fecha }

            tvDetailFechaHora.text = getString(R.string.detail_date_time_format, displayDate, displayTime)
            
            // Limpiar Motivo para que no repita el prefijo si ya está en la etiqueta
            val cleanMotivo = motivo.replace("Motivo de consulta: ", "", ignoreCase = true)
                .replace("Reason for consultation: ", "", ignoreCase = true)
                .trim()
            tvDetailMotivo.text = cleanMotivo
            
            val statusClean = estado.lowercase()
            val isCompleted = statusClean.contains("completado") || statusClean.contains("completed")
            val isCancelled = statusClean.contains("cancelado") || statusClean.contains("cancelled") || statusClean.contains("anulado")
            
            tvDetailStatus.text = when {
                isCompleted -> getString(R.string.status_completed).uppercase()
                isCancelled -> getString(R.string.status_cancelled).uppercase()
                else -> getString(R.string.status_pending).uppercase()
            }
            
            val (statusBg, statusColor) = when {
                isCompleted -> 
                    R.drawable.bg_status_completed to androidx.core.content.ContextCompat.getColor(requireContext(), R.color.status_completed_text)
                isCancelled -> 
                    R.drawable.bg_status_cancelled to androidx.core.content.ContextCompat.getColor(requireContext(), R.color.status_cancelled_text)
                else -> 
                    R.drawable.bg_status_pending to androidx.core.content.ContextCompat.getColor(requireContext(), R.color.status_pending_text)
            }
            
            tvDetailStatus.setBackgroundResource(statusBg)
            tvDetailStatus.setTextColor(statusColor)
            
            if (isCancelled) binding.cardDetail.alpha = 0.8f

            val isPending = !isCompleted && !isCancelled
            btnCancelarTurno.visibility = if (isPending) View.VISIBLE else View.GONE

            btnReagendar.visibility = if (isCancelled) View.VISIBLE else View.GONE
            btnReagendar.setOnClickListener {
                if (isAdded && findNavController().currentDestination?.id == R.id.turnoDetailFragment) {
                    findNavController().navigate(R.id.action_turnoDetailFragment_to_solicitarTurnoFragment)
                }
            }

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            btnVolver.setOnClickListener { findNavController().popBackStack() }

            btnAddToCalendar.setOnClickListener { addToCalendar(paciente, fecha, hora, motivo) }
            btnShareTurno.setOnClickListener { shareTurno(paciente, fecha, hora, motivo) }
            btnOpenMap.setOnClickListener { openMap() }

            btnSetReminder.setOnClickListener {
                val turno = Turno(id, paciente, fecha, hora, motivo, estado)
                reminderManager.scheduleAppointmentReminder(turno)
                Snackbar.make(binding.root, R.string.msg_reminder_set, Snackbar.LENGTH_SHORT).show()
            }

            btnCancelarTurno.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.title_delete_appointment)
                    .setMessage(R.string.msg_delete_confirm)
                    .setPositiveButton(R.string.btn_delete) { _, _ -> viewModel.eliminarTurno(id) }
                    .setNegativeButton(R.string.btn_cancel, null)
                    .show()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!isAdded) return@collect
                    binding.btnCancelarTurno.isEnabled = !state.isLoading
                    binding.btnVolver.isEnabled = !state.isLoading

                    if (state.isDeleteSuccessful) {
                        viewModel.resetState()
                        findNavController().popBackStack()
                    }

                    state.errorMessage?.let { error ->
                        if (error.contains("401") || error.contains("token", true)) {
                            handleSessionExpired()
                        } else {
                            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                        }
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

    private fun shareTurno(paciente: String, fecha: String, hora: String, motivo: String) {
        val displayDate = DateUtils.formatDisplayDate(requireContext(), fecha)
        val displayTime = DateUtils.formatDisplayTime(hora)
        val message = "${getString(R.string.label_share_appointment_header)}\n${getString(R.string.label_share_patient, paciente)}\n${getString(R.string.label_share_date, displayDate)}\n${getString(R.string.label_share_time, displayTime)}\n${getString(R.string.label_share_reason, motivo)}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.label_share_via)))
    }

    private fun openMap() {
        val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=Clinica+Salud+Activa".toUri())
        intent.setPackage("com.google.android.apps.maps")
        try { startActivity(intent) } catch (e: Exception) { Snackbar.make(binding.root, R.string.msg_install_maps, Snackbar.LENGTH_SHORT).show() }
    }

    private fun addToCalendar(paciente: String, fecha: String, hora: String, motivo: String) {
        val cal = Calendar.getInstance()
        try {
            val d = fecha.split("-")
            val timeDate = DateUtils.parseTime(hora)
            if ((d.size == 3) && (timeDate != null)) {
                val timeCal = Calendar.getInstance()
                timeCal.time = timeDate
                cal.set(d[0].toInt(), d[1].toInt()-1, d[2].toInt(), timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE))
                val intent = Intent(Intent.ACTION_INSERT)
                    .setData(android.provider.CalendarContract.Events.CONTENT_URI)
                    .putExtra(android.provider.CalendarContract.Events.TITLE, getString(R.string.label_calendar_event_title, paciente))
                    .putExtra(android.provider.CalendarContract.Events.DESCRIPTION, getString(R.string.label_calendar_event_desc, motivo))
                    .putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.timeInMillis)
                startActivity(intent)
            }
        } catch (e: Exception) { Snackbar.make(binding.root, R.string.msg_no_calendar_app, Snackbar.LENGTH_LONG).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
