package com.example.fitnessdiary.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessdiary.R
import com.example.fitnessdiary.data.model.UserWeight
import com.example.fitnessdiary.databinding.ItemWeightHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WeightHistoryAdapter : ListAdapter<UserWeight, WeightHistoryAdapter.ViewHolder>(WeightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeightHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemWeightHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        
        fun bind(weightRecord: UserWeight) {
            binding.textViewWeight.text = String.format("%.1f kg", weightRecord.weight)
            binding.textViewDate.text = dateFormat.format(weightRecord.date)
        }
    }
}

class WeightDiffCallback : DiffUtil.ItemCallback<UserWeight>() {
    override fun areItemsTheSame(oldItem: UserWeight, newItem: UserWeight): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserWeight, newItem: UserWeight): Boolean {
        return oldItem == newItem
    }
} 