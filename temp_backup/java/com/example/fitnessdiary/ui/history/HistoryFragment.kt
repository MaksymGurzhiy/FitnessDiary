package com.example.fitnessdiary.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessdiary.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up filter button
        binding.buttonFilterDate.setOnClickListener {
            // TODO: Show date picker dialog
        }

        // For now, display "no history" message
        binding.textViewNoHistory.visibility = View.VISIBLE
        binding.recyclerViewHistory.visibility = View.GONE

        // TODO: Set up RecyclerView for workout history
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 