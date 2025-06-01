package com.example.fitnessdiary.ui.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessdiary.R
import com.example.fitnessdiary.data.model.Exercise
import com.example.fitnessdiary.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position], position)
    }

    override fun getItemCount() = exercises.size

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding) : 
        RecyclerView.ViewHolder(binding.root) {
            
        fun bind(exercise: Exercise, position: Int) {
            binding.textViewExerciseName.text = exercise.name
            
            val context = binding.root.context
            binding.textViewSetsReps.text = context.getString(
                R.string.sets_reps_weight_format,
                exercise.sets,
                exercise.reps,
                exercise.weight
            )
            
            if (exercise.notes.isNotBlank()) {
                binding.textViewNotes.visibility = View.VISIBLE
                binding.textViewNotes.text = exercise.notes
            } else {
                binding.textViewNotes.visibility = View.GONE
            }
            
            binding.buttonRemoveExercise.setOnClickListener {
                onRemoveClick(position)
            }
        }
    }
} 