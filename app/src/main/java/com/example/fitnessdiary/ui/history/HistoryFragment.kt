package com.example.fitnessdiary.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessdiary.R
import com.example.fitnessdiary.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var weightAdapter: WeightHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update title
        binding.textViewHistoryTitle.text = getString(R.string.weight_history)

        // Set up filter buttons
        binding.buttonFilterDate.setOnClickListener {
            viewModel.toggleDateSort()
        }
        
        binding.buttonFilterWeight.setOnClickListener {
            viewModel.toggleWeightSort()
        }

        // Set up RecyclerView
        setupRecyclerView()

        // Observe weight history
        viewModel.weightHistory.observe(viewLifecycleOwner) { weightHistory ->
            if (weightHistory.isEmpty()) {
                binding.textViewNoHistory.visibility = View.VISIBLE
                binding.recyclerViewHistory.visibility = View.GONE
                binding.textViewNoHistory.text = getString(R.string.no_weight_history)
            } else {
                binding.textViewNoHistory.visibility = View.GONE
                binding.recyclerViewHistory.visibility = View.VISIBLE
                weightAdapter.submitList(weightHistory)
            }
        }
        
        // Observe date sort order changes
        viewModel.currentDateSortOrder.observe(viewLifecycleOwner) { sortOrder ->
            updateDateFilterButtonText(sortOrder)
        }
        
        // Observe weight sort order changes
        viewModel.currentWeightSortOrder.observe(viewLifecycleOwner) { sortOrder ->
            updateWeightFilterButtonText(sortOrder)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    private fun updateDateFilterButtonText(sortOrder: HistoryViewModel.DateSortOrder) {
        binding.buttonFilterDate.text = when (sortOrder) {
            HistoryViewModel.DateSortOrder.DATE_DESC -> getString(R.string.date_desc)
            HistoryViewModel.DateSortOrder.DATE_ASC -> getString(R.string.date_asc)
        }
    }
    
    private fun updateWeightFilterButtonText(sortOrder: HistoryViewModel.WeightSortOrder) {
        binding.buttonFilterWeight.text = when (sortOrder) {
            HistoryViewModel.WeightSortOrder.NONE -> getString(R.string.filter_by_weight)
            HistoryViewModel.WeightSortOrder.WEIGHT_ASC -> getString(R.string.weight_asc)
            HistoryViewModel.WeightSortOrder.WEIGHT_DESC -> getString(R.string.weight_desc)
        }
    }

    private fun setupRecyclerView() {
        weightAdapter = WeightHistoryAdapter()
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weightAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 