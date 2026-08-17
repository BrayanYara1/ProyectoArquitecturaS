package com.example.gestionturnosapp.ui.turnos

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Turno
import com.example.gestionturnosapp.databinding.ItemTurnoBinding
import com.example.gestionturnosapp.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class TurnosAdapter(
    private val onTurnoClick: (Turno, ItemTurnoBinding) -> Unit,
    private val onDeleteClick: (Turno) -> Unit
) : ListAdapter<Turno, TurnosAdapter.TurnoViewHolder>(TurnoDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnoViewHolder {
        val binding = ItemTurnoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TurnoViewHolder(binding, onTurnoClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: TurnoViewHolder, position: Int) {
        val turno = getItem(position)
        holder.bind(turno)
        
        // Animación de entrada escalonada solo para nuevos elementos al hacer scroll hacia abajo
        val currentPosition = holder.adapterPosition
        if (currentPosition > lastPosition && currentPosition != RecyclerView.NO_POSITION) {
            holder.itemView.alpha = 0f
            holder.itemView.translationY = 50f
            holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay((currentPosition % 12) * 50L)
                .start()
            lastPosition = currentPosition
        }
    }

    class TurnoViewHolder(
        private val binding: ItemTurnoBinding,
        private val onTurnoClick: (Turno, ItemTurnoBinding) -> Unit,
        private val onDeleteClick: (Turno) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(turno: Turno) {
            binding.apply {
                val (specName, specIcon) = getSpecialtyInfo(turno.especialidad)
                tvItemEspecialidad.text = specName
                ivSpecialtyIcon.setImageResource(specIcon)
                
                tvItemDoctor.text = translateDoctor(turno.doctor)
                tvItemMotivo.text = translateMotivo(turno.motivo)
                tvItemNombre.text = turno.pacienteNombre

                // Formatear Hora
                tvItemHora.text = DateUtils.formatDisplayTime(turno.hora)

                // Formatear Fecha
                try {
                    val date = isoDateFormat.parse(turno.fecha)
                    if (date != null) {
                        val cal = Calendar.getInstance()
                        cal.time = date
                        tvItemDiaMes.text = String.format(Locale.getDefault(), "%02d", cal.get(Calendar.DAY_OF_MONTH))
                        tvItemMesCorta.text = getMonthFormat().format(date).uppercase()
                    }
                } catch (_: Exception) {
                    tvItemDiaMes.text = root.context.getString(R.string.label_error_date_short)
                    tvItemMesCorta.text = root.context.getString(R.string.label_error_month_short)
                }
                
                // Aplicar estilo de estado usando la data class StatusStyle
                val style = getStatusStyle(turno.estado)
                
                tvItemStatus.text = style.label
                tvItemStatus.backgroundTintList = ColorStateList.valueOf(style.bgColor)
                tvItemStatus.setTextColor(style.textColor)
                ivSpecialtyIcon.imageTintList = ColorStateList.valueOf(style.iconTint)
                
                // Indicador lateral sutil
                indicatorSpecialty.backgroundTintList = ColorStateList.valueOf(style.iconTint)

                // Efectos para cancelados
                val isCancelled = turno.estado.lowercase().contains("canc")
                root.alpha = if (isCancelled) 0.6f else 1.0f
                btnDeleteItem.isVisible = !isCancelled
                root.cardElevation = if (isCancelled) 0f else 6f
                
                // Stroke dinámico para resaltar
                root.strokeColor = if (isCancelled) 
                    ContextCompat.getColor(root.context, R.color.outline)
                else 
                    style.iconTint
                root.strokeWidth = if (isCancelled) 1 else 2

                root.transitionName = "card_${turno.id}"
                tvItemNombre.transitionName = "name_${turno.id}"
                dateContainer.transitionName = "date_${turno.id}"

                root.setOnClickListener { 
                    it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                    onTurnoClick(turno, binding) 
                }
                btnDeleteItem.setOnClickListener { 
                    it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                    onDeleteClick(turno) 
                }
            }
        }

        private fun getStatusStyle(estado: String): StatusStyle {
            val context = binding.root.context
            val e = estado.lowercase()
            return when {
                e.contains("compl") || e.contains("final") || e.contains("done") -> StatusStyle(
                    context.getString(R.string.status_completed).uppercase(),
                    ContextCompat.getColor(context, R.color.status_completed_bg),
                    ContextCompat.getColor(context, R.color.status_completed_text),
                    ContextCompat.getColor(context, R.color.success)
                )
                e.contains("canc") || e.contains("anul") || e.contains("abort") -> StatusStyle(
                    context.getString(R.string.status_cancelled).uppercase(),
                    ContextCompat.getColor(context, R.color.status_cancelled_bg),
                    ContextCompat.getColor(context, R.color.status_cancelled_text),
                    ContextCompat.getColor(context, R.color.error)
                )
                else -> StatusStyle(
                    context.getString(R.string.status_pending).uppercase(),
                    ContextCompat.getColor(context, R.color.status_pending_bg),
                    ContextCompat.getColor(context, R.color.status_pending_text),
                    ContextCompat.getColor(context, R.color.primary)
                )
            }
        }

        private fun getSpecialtyInfo(specialty: String?): Pair<String, Int> {
            val context = binding.root.context
            val name = when (specialty?.lowercase()) {
                "cardiología", "cardiology" -> context.getString(R.string.name_cardiology)
                "pediatría", "pediatrics" -> context.getString(R.string.name_pediatrics)
                "traumatología", "traumatology" -> context.getString(R.string.name_traumatology)
                "dermatología", "dermatology" -> context.getString(R.string.name_dermatology)
                "neurología", "neurology" -> context.getString(R.string.name_neurology)
                else -> specialty ?: context.getString(R.string.label_default_specialty)
            }
            
            val icon = when (specialty?.lowercase()) {
                "cardiología", "cardiology" -> R.drawable.ic_specialty_cardiology
                "pediatría", "pediatrics" -> R.drawable.ic_specialty_pediatrics
                "traumatología", "traumatology" -> R.drawable.ic_specialty_traumatology
                "dermatología", "dermatology" -> R.drawable.ic_specialty_dermatology
                "neurología", "neurology" -> R.drawable.ic_specialty_neurology
                else -> R.drawable.ic_nav_calendar
            }
            
            return Pair(name, icon)
        }

        private fun translateDoctor(doctor: String?): String {
            val context = binding.root.context
            if (doctor.isNullOrBlank() || doctor.lowercase().contains("asignado") || doctor.lowercase().contains("assigned")) {
                return context.getString(R.string.label_assigned_doctor)
            }
            return if (!doctor.startsWith("Dr.") && !doctor.startsWith("Dra.")) "Dr. $doctor" else doctor
        }

        private fun translateMotivo(motivo: String?): String {
            val context = binding.root.context
            if (motivo == null) return ""
            
            val prefixes = listOf("Motivo de consulta: ", "Reason for consultation: ")
            for (prefix in prefixes) {
                if (motivo.startsWith(prefix)) {
                    val content = motivo.substring(prefix.length)
                    val (translatedName, _) = getSpecialtyInfo(content)
                    return context.getString(R.string.reason_consultation_for, translatedName)
                }
            }
            return motivo
        }

        companion object {
            private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            
            // Usamos una función para obtener el formato y respetar el Locale actual del sistema
            private fun getMonthFormat() = SimpleDateFormat("MMM", Locale.getDefault())
        }
    }

    /**
     * Representa el estilo visual de un estado de turno.
     */
    private data class StatusStyle(
        val label: String,
        val bgColor: Int,
        val textColor: Int,
        val iconTint: Int
    )

    class TurnoDiffCallback : DiffUtil.ItemCallback<Turno>() {
        override fun areItemsTheSame(oldItem: Turno, newItem: Turno): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Turno, newItem: Turno): Boolean = oldItem == newItem
    }
}
