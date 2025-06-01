package com.example.fitnessdiary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessdiary.R
import com.example.fitnessdiary.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up current weight display
        binding.textViewCurrentWeight.text = getString(R.string.current_weight, "75.0")

        // Set up update weight button
        binding.buttonUpdateWeight.setOnClickListener {
            // TODO: Show weight update dialog
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