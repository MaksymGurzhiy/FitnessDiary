package com.example.fitnessdiary.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitnessdiary.databinding.FragmentWorkoutBinding

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up create workout button
        binding.buttonCreateWorkout.setOnClickListener {
            val action = WorkoutFragmentDirections.actionWorkoutFragmentToCreateWorkoutFragment()
            findNavController().navigate(action)
        }

        // For now, display "no workouts" message
        binding.textViewNoWorkouts.visibility = View.VISIBLE
        binding.recyclerViewWorkouts.visibility = View.GONE

        // TODO: Set up RecyclerView for workouts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 