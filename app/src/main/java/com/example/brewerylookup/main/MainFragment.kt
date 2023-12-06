package com.example.brewerylookup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.FragmentMainBinding
import com.example.brewerylookup.model.BreweryList
import com.example.inventory.common.BaseFragment
import com.example.inventory.common.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: BaseFragment() {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        viewModel.loading.observe(viewLifecycleOwner) { setupLoading(it) }
        viewModel.snackBar.observe(viewLifecycleOwner) { showSnackBar(it) }
        viewModel.breweryList.observe(viewLifecycleOwner) { setupBreweryListCount(it) }

    }

    private fun setupListeners() {
        binding.actionSearch.setOnClickListener {
            viewModel.searchAllBreweries()
        }
    }

    private fun setupBreweryListCount(breweryList: List<BreweryList>) {
        binding.breweryAmount.text = getString(R.string.brewery_list_amount, breweryList.size)
    }

    private fun setupLoading(event: Event<Boolean?>) {
        event.contentIfNotHandled?.let {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }
}