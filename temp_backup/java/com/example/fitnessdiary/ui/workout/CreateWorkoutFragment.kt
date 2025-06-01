package com.example.fitnessdiary.ui.workout

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessdiary.R
import com.example.fitnessdiary.data.model.Exercise
import com.example.fitnessdiary.data.model.Workout
import com.example.fitnessdiary.databinding.DialogAddExerciseBinding
import com.example.fitnessdiary.databinding.FragmentCreateWorkoutBinding
import com.example.fitnessdiary.ui.workout.ExerciseAdapter

class CreateWorkoutFragment : Fragment() {

    private var _binding: FragmentCreateWorkoutBinding? = null
    private val binding get() = _binding!!
    private val args: CreateWorkoutFragmentArgs by navArgs()
    
    private val exercises = mutableListOf<Exercise>()
    private lateinit var exerciseAdapter: ExerciseAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupDayOfWeekSpinner()
        setupExerciseList()
        
        // Check if we're editing an existing workout
        if (args.workoutId != -1) {
            // TODO: Load workout data
        }
        
        binding.buttonAddExercise.setOnClickListener {
            showAddExerciseDialog()
        }
        
        binding.buttonSave.setOnClickListener {
            saveWorkout()
        }
    }
    
    private fun setupDayOfWeekSpinner() {
        val days = resources.getStringArray(R.array.days_of_week)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)
        binding.spinnerDayOfWeek.adapter = adapter
    }
    
    private fun setupExerciseList() {
        exerciseAdapter = ExerciseAdapter(exercises) { position ->
            exercises.removeAt(position)
            exerciseAdapter.notifyItemRemoved(position)
        }
        
        binding.recyclerViewExercises.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exerciseAdapter
        }
    }
    
    private fun showAddExerciseDialog() {
        val dialogBinding = DialogAddExerciseBinding.inflate(layoutInflater)
        
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_exercise)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                try {
                    val name = dialogBinding.editTextExerciseName.text.toString()
                    val sets = dialogBinding.editTextSets.text.toString().toInt()
                    val reps = dialogBinding.editTextReps.text.toString().toInt()
                    val weight = dialogBinding.editTextWeight.text.toString().toFloat()
                    val notes = dialogBinding.editTextNotes.text.toString()
                    
                    if (name.isBlank()) {
                        Toast.makeText(context, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    
                    val exercise = Exercise(name, sets, reps, weight, notes)
                    exercises.add(exercise)
                    exerciseAdapter.notifyItemInserted(exercises.size - 1)
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    
    private fun saveWorkout() {
        val name = binding.editTextWorkoutName.text.toString()
        val dayOfWeek = binding.spinnerDayOfWeek.selectedItemPosition + 1 // 1-based (Monday = 1)
        
        if (name.isBlank() || exercises.isEmpty()) {
            Toast.makeText(context, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
            return
        }
        
        val workout = Workout(
            id = if (args.workoutId != -1) args.workoutId else 0,
            name = name,
            dayOfWeek = dayOfWeek,
            exercises = exercises
        )
        
        // TODO: Save workout to database
        
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 