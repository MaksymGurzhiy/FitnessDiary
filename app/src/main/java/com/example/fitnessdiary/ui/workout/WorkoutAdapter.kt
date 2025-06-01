package com.example.fitnessdiary.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessdiary.R
import com.example.fitnessdiary.data.model.Workout
import com.example.fitnessdiary.databinding.ItemWorkoutBinding

class WorkoutAdapter(private val onWorkoutClick: (Workout) -> Unit) : 
    ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onWorkoutClick(getItem(position))
                }
            }
        }

        fun bind(workout: Workout) {
            val context = binding.root.context
            
            // Set day of week
            val daysOfWeek = context.resources.getStringArray(R.array.days_of_week)
            val dayIndex = workout.dayOfWeek - 1 // Convert 1-based to 0-based
            binding.textViewDayOfWeek.text = if (dayIndex in daysOfWeek.indices) {
                daysOfWeek[dayIndex]
            } else {
                "Unknown Day"
            }
            
            // Set workout name
            binding.textViewWorkoutName.text = workout.name
            
            // Display number of exercises
            binding.textViewExerciseCount.text = context.getString(
                R.string.exercise_count, 
                workout.exercises.size
            )
        }
    }

    private class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }
} 