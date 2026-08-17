package com.example.gestionturnosapp.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionturnosapp.data.model.Achievement
import com.example.gestionturnosapp.databinding.ItemAchievementBinding

class AchievementsAdapter : ListAdapter<Achievement, AchievementsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: Achievement) {
            binding.tvAchievementTitle.text = achievement.title
            binding.tvAchievementDesc.text = achievement.description
            binding.ivAchievementIcon.setImageResource(achievement.iconResId)
            
            binding.pbAchievement.max = achievement.target
            binding.pbAchievement.progress = achievement.progress
            
            binding.ivLocked.isVisible = !achievement.isUnlocked
            binding.root.alpha = if (achievement.isUnlocked) 1.0f else 0.6f
            
            if (achievement.isUnlocked) {
                binding.ivAchievementIcon.imageTintList = null
            } else {
                binding.ivAchievementIcon.setColorFilter(android.graphics.Color.GRAY)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem == newItem
    }
}
