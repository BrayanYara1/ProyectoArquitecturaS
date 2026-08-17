package com.example.gestionturnosapp.ui.especialidades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Especialidad
import com.example.gestionturnosapp.databinding.ItemEspecialidadBinding

class EspecialidadesAdapter(
    private val onItemClick: (Especialidad) -> Unit
) : ListAdapter<Especialidad, EspecialidadesAdapter.ViewHolder>(EspecialidadDiffCallback()) {

    class ViewHolder(val binding: ItemEspecialidadBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEspecialidadBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            val context = root.context
            val nombre = context.getString(item.nombreRes)
            tvEspecialidadNombre.text = nombre
            tvEspecialidadDesc.text = context.getString(item.descripcionRes)
            ivEspecialidadIcon.setImageResource(item.iconoResId)
            
            // Dynamic colors based on specialty for Elite look
            val (tint, bg) = when {
                nombre.contains("Cardio", true) -> 
                    Pair(context.getColor(R.color.error), context.getColor(R.color.status_cancelled_bg))
                nombre.contains("Pedia", true) -> 
                    Pair(context.getColor(R.color.secondary), context.getColor(R.color.secondary_container))
                nombre.contains("Trauma", true) -> 
                    Pair(context.getColor(R.color.primary), context.getColor(R.color.primary_container))
                nombre.contains("Derma", true) -> 
                    Pair(context.getColor(R.color.vibrant_blue), context.getColor(R.color.primary_container))
                nombre.contains("Neuro", true) -> 
                    Pair(context.getColor(R.color.on_secondary_container), context.getColor(R.color.secondary_container))
                else -> 
                    Pair(context.getColor(R.color.primary), context.getColor(R.color.primary_container))
            }
            
            ivEspecialidadIcon.imageTintList = android.content.res.ColorStateList.valueOf(tint)
            iconBg.backgroundTintList = android.content.res.ColorStateList.valueOf(bg)

            root.setOnClickListener { 
                it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                onItemClick(item) 
            }
        }
        
        // Premium Entrance Animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 100f
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(position * 40L)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()
    }

    class EspecialidadDiffCallback : DiffUtil.ItemCallback<Especialidad>() {
        override fun areItemsTheSame(oldItem: Especialidad, newItem: Especialidad): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Especialidad, newItem: Especialidad): Boolean {
            return oldItem == newItem
        }
    }
}
