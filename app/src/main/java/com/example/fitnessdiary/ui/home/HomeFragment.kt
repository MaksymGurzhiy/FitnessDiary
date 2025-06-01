package com.example.fitnessdiary.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessdiary.R
import com.example.fitnessdiary.data.model.Workout
import com.example.fitnessdiary.databinding.FragmentHomeBinding
import com.example.fitnessdiary.databinding.DialogUpdateWeightBinding
import com.example.fitnessdiary.ui.workout.WorkoutAdapter
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var workoutAdapter: WorkoutAdapter
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        setupRecyclerView()
        
        // Setup day tabs
        setupDayTabs()

        // Observe weight changes
        viewModel.currentWeight.observe(viewLifecycleOwner) { weight ->
            binding.textViewCurrentWeight.text = getString(R.string.current_weight, weight.toString())      
        }

        // Observe selected day
        viewModel.selectedDayOfWeek.observe(viewLifecycleOwner) { dayIndex ->
            // Update UI title
            val daysOfWeek = resources.getStringArray(R.array.days_of_week)
            val dayName = if (dayIndex > 0 && dayIndex <= daysOfWeek.size) {
                daysOfWeek[dayIndex - 1]
            } else {
                getString(R.string.today)
            }
            binding.textViewWorkoutsTitle.text = getString(R.string.workouts_for_day, dayName)
            
            // Select the corresponding tab
            if (dayIndex > 0 && dayIndex <= 7) {
                binding.tabLayoutDays.getTabAt(dayIndex - 1)?.select()
            }
        }

        // Observe workouts for selected day
        viewModel.workoutsForSelectedDay.observe(viewLifecycleOwner) { workouts ->
            updateWorkoutsUI(workouts)
        }

        // Set up update weight button
        binding.buttonUpdateWeight.setOnClickListener {
            showWeightUpdateDialog()
        }
    }
    
    private fun setupDayTabs() {
        val daysOfWeek = resources.getStringArray(R.array.days_of_week)
        
        // Clear existing tabs
        binding.tabLayoutDays.removeAllTabs()
        
        // Add a tab for each day
        for (day in daysOfWeek) {
            binding.tabLayoutDays.addTab(binding.tabLayoutDays.newTab().setText(day))
        }
        
        // Listen for tab selections
        binding.tabLayoutDays.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Day indexes are 1-based, so add 1 to the tab position
                viewModel.selectDay(tab.position + 1)
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter { workout ->
            // Navigate to edit workout screen
            val action = HomeFragmentDirections.actionNavigationHomeToCreateWorkoutFragment(workout.id)
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

    private fun showWeightUpdateDialog() {
        val dialogBinding = DialogUpdateWeightBinding.inflate(layoutInflater)
        
        // Set current weight as default value
        viewModel.currentWeight.value?.let {
            dialogBinding.editTextWeight.setText(it.toString())
        }
        
        // Set current date as default
        calendar.time = Date() // Reset to today
        updateDateButtonText(dialogBinding)
        
        // Set up date selection
        dialogBinding.buttonSelectDate.setOnClickListener {
            showDatePicker(dialogBinding)
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.update_weight)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                try {
                    val weightText = dialogBinding.editTextWeight.text.toString()
                    if (weightText.isNotEmpty()) {
                        val weight = weightText.toFloat()
                        viewModel.updateWeight(weight, calendar.time)
                    }
                } catch (e: NumberFormatException) {
                    // Handle invalid input
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    
    private fun showDatePicker(dialogBinding: DialogUpdateWeightBinding) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            updateDateButtonText(dialogBinding)
        }, year, month, day).show()
    }
    
    private fun updateDateButtonText(dialogBinding: DialogUpdateWeightBinding) {
        val formattedDate = dateFormat.format(calendar.time)
        dialogBinding.buttonSelectDate.text = getString(R.string.date_selected, formattedDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 