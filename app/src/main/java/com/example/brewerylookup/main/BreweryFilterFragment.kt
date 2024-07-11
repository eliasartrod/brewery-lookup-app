package com.example.brewerylookup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brewerylookup.databinding.FragmentFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreweryFilterFragment : DialogFragment() {
    private lateinit var binding: FragmentFilterBinding

    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionApply.setOnClickListener {
            // Update the filter parameters in the BreweryFilter instance
            val breweryFilter = viewModel.filter

            breweryFilter.breweryType = binding.spinnerBreweryType.selectedItem?.toString()
            breweryFilter.breweryState = binding.spinnerState.selectedItem?.toString()
            breweryFilter.breweryPostalCode = binding.editTextPostalCode.text?.toString()
            breweryFilter.breweryCity = binding.editTextCity.text?.toString()
            breweryFilter.breweryName = binding.editTextName.text?.toString()

            // Perform the search with the updated filter
            viewModel.searchByFilter(breweryFilter)

            dismiss()
        }

        binding.actionNoFilter.setOnClickListener {
            loadDefault()
            dismiss()
        }

        binding.actionCancel.setOnClickListener {
            loadDefault()
            dismiss()
        }
    }

    private fun loadDefault() {
        viewModel.searchAllBreweries(1, 10)
    }
}
