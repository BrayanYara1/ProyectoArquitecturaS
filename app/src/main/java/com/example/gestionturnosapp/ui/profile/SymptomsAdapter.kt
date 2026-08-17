package com.example.gestionturnosapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.data.model.SymptomRecord
import com.example.gestionturnosapp.databinding.ItemSymptomBinding
import java.text.SimpleDateFormat
import java.util.*

class SymptomsAdapter(
    private val onDeleteClick: (SymptomRecord) -> Unit
) : ListAdapter<SymptomRecord, SymptomsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemSymptomBinding,
        private val onDeleteClick: (SymptomRecord) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: SymptomRecord) {
            binding.tvSymptomName.text = record.description
            binding.tvSymptomIntensity.text = "Intensidad: ${record.intensity}/10"
            
            val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
            binding.tvSymptomDate.text = sdf.format(Date(record.date))

            binding.root.setOnLongClickListener {
                onDeleteClick(record)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SymptomRecord>() {
        override fun areItemsTheSame(oldItem: SymptomRecord, newItem: SymptomRecord) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SymptomRecord, newItem: SymptomRecord) = oldItem == newItem
    }
}
