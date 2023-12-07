package com.example.brewerylookup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brewerylookup.databinding.FragmentFilterBinding
import com.example.brewerylookup.model.FilterModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : DialogFragment() {
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
            // Create a FilterModel with the selected filter parameters
            viewModel.searchByFilter(
                binding.spinnerBreweryType.selectedItem?.toString(),
                binding.spinnerState.selectedItem?.toString(),
                binding.editTextPostalCode.text?.toString(),
                binding.editTextCity.text?.toString(),
                binding.editTextName.text?.toString()
            )
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
        viewModel.searchAllBreweries(1, 5000)
    }
}
