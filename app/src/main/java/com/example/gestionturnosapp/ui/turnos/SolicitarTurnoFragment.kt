package com.example.gestionturnosapp.ui.turnos

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.util.Resource
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.databinding.FragmentSolicitarTurnoBinding
import com.example.gestionturnosapp.util.DateUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import com.google.android.material.transition.MaterialFadeThrough

@AndroidEntryPoint
class SolicitarTurnoFragment : Fragment() {

    private var _binding: FragmentSolicitarTurnoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TurnosListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    @Inject
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitarTurnoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Manejar insets para diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updatePadding(top = systemBars.top)
            insets
        }

        setupInitialData()
        setupObservers()
        setupValidationListeners()
        setupPickers()
        setupOnBackPressed()
        
        binding.etFecha.isFocusable = false
        binding.etHora.isFocusable = false
        binding.tilFecha.isClickable = true
        binding.tilHora.isClickable = true
        binding.etFecha.isClickable = true
        binding.etHora.isClickable = true

        binding.btnConfirmarTurno.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            
            val fecha = viewModel.formFecha.value ?: ""
            val hora = viewModel.formHora.value ?: ""

            if (DateUtils.isPastDateTime(fecha, hora)) {
                Snackbar.make(binding.root, R.string.msg_past_date, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            val displayFecha = DateUtils.formatDisplayDate(requireContext(), fecha)
            val displayHora = DateUtils.formatDisplayTime(hora)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.msg_confirm_booking_title)
                .setMessage(getString(R.string.msg_confirm_booking_body, displayFecha, displayHora))
                .setPositiveButton(R.string.btn_confirm) { _, _ ->
                    val especialidadArg = arguments?.getString("especialidadNombre")
                    viewModel.crearNuevoTurno(
                        especialidad = especialidadArg,
                        doctor = getString(R.string.label_assigned_doctor)
                    )
                }
                .setNegativeButton(R.string.btn_cancel, null)
                .show()
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun setupInitialData() {
        val currentUser = userManager.getUser()
        if (viewModel.formPacienteNombre.value.isNullOrBlank()) {
            currentUser?.let {
                viewModel.formPacienteNombre.value = it.nombre
                binding.etPacienteNombre.setText(it.nombre)
            }
        } else {
            binding.etPacienteNombre.setText(viewModel.formPacienteNombre.value)
        }

        val especialidadArg = arguments?.getString("especialidadNombre")
        if (especialidadArg != null && viewModel.formMotivo.value.isNullOrBlank()) {
            val motivo = getString(R.string.reason_consultation_for, especialidadArg)
            viewModel.formMotivo.value = motivo
            binding.etMotivo.setText(motivo)
        } else {
            binding.etMotivo.setText(viewModel.formMotivo.value)
        }

        binding.etFecha.setText(viewModel.formFecha.value)
        binding.etHora.setText(viewModel.formHora.value)
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                if (hayCambiosSinGuardar()) {
                    showDiscardDialog()
                } else {
                    isEnabled = false
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun hayCambiosSinGuardar(): Boolean {
        val nombre = binding.etPacienteNombre.text.toString().trim()
        val fecha = binding.etFecha.text.toString().trim()
        val hora = binding.etHora.text.toString().trim()
        val motivo = binding.etMotivo.text.toString().trim()
        
        val user = userManager.getUser()
        val especialidadArg = arguments?.getString("especialidadNombre")
        val motivoDefault = if (especialidadArg != null) getString(R.string.reason_consultation_for, especialidadArg) else ""
        
        return (nombre != (user?.nombre ?: "").trim() && nombre.isNotEmpty()) || 
               fecha.isNotEmpty() || 
               hora.isNotEmpty() || 
               (motivo != motivoDefault.trim() && motivo.isNotEmpty())
    }

    private fun showDiscardDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.msg_discard_title)
            .setMessage(R.string.msg_discard_message)
            .setNegativeButton(R.string.btn_keep_editing, null)
            .setPositiveButton(R.string.btn_discard) { _, _ ->
                viewModel.resetNavegacion() // Limpiar VM al descartar
                findNavController().popBackStack()
            }
            .show()
    }

    private fun setupPickers() {
        val dateClickListener = View.OnClickListener {
            val constraints = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.hint_date)
                .setCalendarConstraints(constraints)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val formattedDate = sdf.format(selection)
                
                binding.etFecha.setText(formattedDate)
                viewModel.formFecha.value = formattedDate
                binding.tilFecha.error = null
                validarDebounced()
            }

            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        binding.etFecha.setOnClickListener(dateClickListener)
        binding.tilFecha.setOnClickListener(dateClickListener)

        val timeClickListener = View.OnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(10)
                .setMinute(0)
                .setTitleText(R.string.hint_time)
                .build()

            picker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = picker.hour
                calendar[Calendar.MINUTE] = picker.minute
                
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val formattedTime = sdf.format(calendar.time)
                
                binding.etHora.setText(formattedTime)
                viewModel.formHora.value = formattedTime
                binding.tilHora.error = null
                validarDebounced()
            }
            picker.show(childFragmentManager, "TIME_PICKER")
        }

        binding.etHora.setOnClickListener(timeClickListener)
        binding.tilHora.setOnClickListener(timeClickListener)
    }



    private fun setupValidationListeners() {
        binding.etPacienteNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { viewModel.formPacienteNombre.value = s.toString() }
        })
        binding.etFecha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { 
                viewModel.formFecha.value = s.toString()
                validarDebounced() 
            }
        })
        binding.etHora.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { 
                viewModel.formHora.value = s.toString()
                validarDebounced() 
            }
        })
        binding.etMotivo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { viewModel.formMotivo.value = s.toString() }
        })
    }

    private var availabilityCheckJob: kotlinx.coroutines.Job? = null
    private fun validarDebounced() {
        availabilityCheckJob?.cancel()
        availabilityCheckJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(400)
            if (isActive) {
                val fecha = viewModel.formFecha.value ?: ""
                val hora = viewModel.formHora.value ?: ""
                if (fecha.isNotEmpty() && hora.isNotEmpty()) {
                    viewModel.verificarDisponibilidad(fecha, hora)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.isSlotAvailable.observe(viewLifecycleOwner) { disponible ->
            if (isAdded) actualizarEstadoBotonDisponibilidad(disponible)
        }

        viewModel.formPacienteNombre.observe(viewLifecycleOwner) { actualizarEstadoBotonDisponibilidad(viewModel.isSlotAvailable.value) }
        viewModel.formFecha.observe(viewLifecycleOwner) { actualizarEstadoBotonDisponibilidad(viewModel.isSlotAvailable.value) }
        viewModel.formHora.observe(viewLifecycleOwner) { actualizarEstadoBotonDisponibilidad(viewModel.isSlotAvailable.value) }
        viewModel.formMotivo.observe(viewLifecycleOwner) { actualizarEstadoBotonDisponibilidad(viewModel.isSlotAvailable.value) }


        viewModel.createTurnoResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnConfirmarTurno.isEnabled = false
                    binding.btnConfirmarTurno.text = getString(R.string.btn_processing)
                }
                is Resource.Success -> {
                    Snackbar.make(binding.root, getString(R.string.msg_appointment_success), Snackbar.LENGTH_LONG).show()
                    viewModel.resetNavegacion()
                    findNavController().popBackStack()
                }
                is Resource.Error -> {
                    binding.btnConfirmarTurno.isEnabled = true
                    actualizarEstadoBotonDisponibilidad(viewModel.isSlotAvailable.value)
                    val msg = resource.message
                    if (msg.contains("401") || msg.contains("token", ignoreCase = true)) {
                        handleSessionExpired()
                    } else {
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                    }
                }
                else -> {}
            }
        }
    }

    private fun handleSessionExpired() {
        if (isAdded && findNavController().currentDestination?.id == R.id.solicitarTurnoFragment) {
            userManager.logout()
            findNavController().navigate(R.id.loginFragment, null, androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build())
        }
    }

    private fun actualizarEstadoBotonDisponibilidad(disponible: Boolean?) {
        val context = context ?: return
        val nombre = viewModel.formPacienteNombre.value ?: ""
        val fecha = viewModel.formFecha.value ?: ""
        val hora = viewModel.formHora.value ?: ""
        val motivo = viewModel.formMotivo.value ?: ""
        
        val isFormFilled = nombre.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && motivo.isNotBlank()
        
        when (disponible) {
            true -> {
                binding.btnConfirmarTurno.isEnabled = isFormFilled
                binding.btnConfirmarTurno.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary))
                binding.btnConfirmarTurno.text = getString(R.string.msg_slot_available)
            }
            false -> {
                binding.btnConfirmarTurno.isEnabled = false
                binding.btnConfirmarTurno.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.error))
                binding.btnConfirmarTurno.text = getString(R.string.msg_slot_occupied)
            }
            else -> {
                binding.btnConfirmarTurno.isEnabled = isFormFilled
                binding.btnConfirmarTurno.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary))
                binding.btnConfirmarTurno.text = if (isFormFilled) getString(R.string.btn_processing) else getString(R.string.btn_confirm_appointment)

            }
        }
        binding.btnConfirmarTurno.alpha = if (binding.btnConfirmarTurno.isEnabled) 1.0f else 0.6f
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
