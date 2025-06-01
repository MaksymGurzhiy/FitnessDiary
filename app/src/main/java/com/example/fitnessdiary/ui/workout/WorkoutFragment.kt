package com.example.fitnessdiary.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessdiary.data.model.Workout
import com.example.fitnessdiary.databinding.FragmentWorkoutBinding

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        setupRecyclerView()
        
        // Observe all workouts
        viewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            updateWorkoutsUI(workouts)
        }

        // Set up create workout button
        binding.buttonCreateWorkout.setOnClickListener {
            val action = WorkoutFragmentDirections.actionWorkoutFragmentToCreateWorkoutFragment()
            findNavController().navigate(action)
        }
    }
    
    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter { workout ->
            // Navigate to edit workout screen
            val action = WorkoutFragmentDirections.actionWorkoutFragmentToCreateWorkoutFragment(workout.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }
    }
    
    private fun updateWorkoutsUI(workouts: List<Workout>) {
        if (workouts.isEmpty()) {
            binding.textViewNoWorkouts.visibility = View.VISIBLE
            binding.recyclerViewWorkouts.visibility = View.GONE
        } else {
            binding.textViewNoWorkouts.visibility = View.GONE
            binding.recyclerViewWorkouts.visibility = View.VISIBLE
            workoutAdapter.submitList(workouts)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        viewModel.loadWorkouts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 