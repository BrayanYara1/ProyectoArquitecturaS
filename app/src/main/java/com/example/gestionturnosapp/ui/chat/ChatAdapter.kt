package com.example.gestionturnosapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.data.model.Mensaje
import com.example.gestionturnosapp.databinding.ItemChatMessageMeBinding
import com.example.gestionturnosapp.databinding.ItemChatMessageOtherBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter : ListAdapter<Mensaje, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_ME = 1
        private const val TYPE_OTHER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).remitente == "PACIENTE") TYPE_ME else TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ME) {
            MeViewHolder(ItemChatMessageMeBinding.inflate(inflater, parent, false))
        } else {
            OtherViewHolder(ItemChatMessageOtherBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = getItem(position)
        if (holder is MeViewHolder) holder.bind(mensaje)
        else if (holder is OtherViewHolder) holder.bind(mensaje)
    }

    class MeViewHolder(private val binding: ItemChatMessageMeBinding) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        fun bind(mensaje: Mensaje) {
            binding.tvMessage.text = mensaje.texto
            binding.tvTime.text = timeFormat.format(mensaje.fecha)
        }
    }

    class OtherViewHolder(private val binding: ItemChatMessageOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        fun bind(mensaje: Mensaje) {
            binding.tvMessage.text = mensaje.texto
            binding.tvTime.text = timeFormat.format(mensaje.fecha)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Mensaje>() {
        override fun areItemsTheSame(oldItem: Mensaje, newItem: Mensaje) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Mensaje, newItem: Mensaje) = oldItem == newItem
    }
}
