package com.example.gestionturnosapp.ui.medicamentos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.databinding.ItemMedicationHomeBinding

class MedicamentosAdapter(
    private val onItemClick: (Medicamento) -> Unit,
    private val onDeleteClick: (Medicamento) -> Unit,
    private val onTakeClick: (Medicamento) -> Unit
) : ListAdapter<Medicamento, MedicamentosAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMedicationHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onDeleteClick, onTakeClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val med = getItem(position)
        holder.bind(med)
        
        holder.itemView.alpha = 0f
        holder.itemView.translationX = -50f
        holder.itemView.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(350)
            .setStartDelay(position * 40L)
            .start()
    }

    class ViewHolder(
        private val binding: ItemMedicationHomeBinding,
        private val onItemClick: (Medicamento) -> Unit,
        private val onDeleteClick: (Medicamento) -> Unit,
        private val onTakeClick: (Medicamento) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(med: Medicamento) {
            val context = binding.root.context
            binding.tvMedName.text = context.getString(com.example.gestionturnosapp.R.string.label_medication_format, med.nombre, med.dosis)
            
            val stockText = if (med.stockActual > 0) " | Stock: ${med.stockActual}" else " | ¡SIN STOCK!"
            binding.tvMedSchedule.text = med.frecuencia + context.getString(com.example.gestionturnosapp.R.string.label_next_dose, med.proximaToma) + stockText
            
            if (med.stockActual <= med.stockMinimo) {
                binding.tvMedSchedule.setTextColor(android.graphics.Color.RED)
            } else {
                binding.tvMedSchedule.setTextColor(context.getColor(com.example.gestionturnosapp.R.color.text_secondary))
            }
            
            binding.btnDeleteMed.visibility = android.view.View.VISIBLE
            binding.ivMedInfo.visibility = android.view.View.VISIBLE
            binding.ivMedInfo.setImageResource(android.R.drawable.checkbox_off_background)
            
            binding.root.setOnClickListener {
                onItemClick(med)
            }

            binding.btnDeleteMed.setOnClickListener {
                onDeleteClick(med)
            }

            binding.ivMedInfo.setOnClickListener {
                it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                onTakeClick(med)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Medicamento>() {
        override fun areItemsTheSame(oldItem: Medicamento, newItem: Medicamento): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Medicamento, newItem: Medicamento): Boolean = oldItem == newItem
    }
}
