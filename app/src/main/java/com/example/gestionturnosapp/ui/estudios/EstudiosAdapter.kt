package com.example.gestionturnosapp.ui.estudios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gestionturnosapp.data.model.EstudioMedico
import com.example.gestionturnosapp.databinding.ItemEstudioBinding

class EstudiosAdapter(
    private val onItemClick: (EstudioMedico) -> Unit,
    private val onDeleteClick: (EstudioMedico) -> Unit
) : ListAdapter<EstudioMedico, EstudiosAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEstudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemEstudioBinding,
        private val onItemClick: (EstudioMedico) -> Unit,
        private val onDeleteClick: (EstudioMedico) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(estudio: EstudioMedico) {
            binding.apply {
                tvEstudioTitulo.text = estudio.titulo
                tvEstudioTipo.text = estudio.tipo.uppercase()
                tvEstudioResultado.text = root.context.getString(com.example.gestionturnosapp.R.string.label_study_result, estudio.resultadoBreve)
                tvEstudioFecha.text = estudio.fecha
                
                if (!estudio.urlDocumento.isNullOrEmpty()) {
                    ivEstudioAdjunto.visibility = android.view.View.VISIBLE
                    ivEstudioAdjunto.load(estudio.urlDocumento)
                } else {
                    ivEstudioAdjunto.visibility = android.view.View.GONE
                }

                root.setOnClickListener {
                    onItemClick(estudio)
                }

                btnDeleteEstudio.setOnClickListener {
                    onDeleteClick(estudio)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EstudioMedico>() {
        override fun areItemsTheSame(oldItem: EstudioMedico, newItem: EstudioMedico): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: EstudioMedico, newItem: EstudioMedico): Boolean = oldItem == newItem
    }
}
