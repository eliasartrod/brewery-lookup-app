package com.example.brewerylookup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.BreweryListCardBinding
import com.example.brewerylookup.databinding.FragmentMainBinding
import com.example.brewerylookup.model.BreweryList
import com.example.inventory.common.BaseFragment
import com.example.inventory.common.Event
import com.example.inventory.common.SnackBarMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var breweryListAdapter: BreweryListAdapter

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
        viewModel.breweryList.observe(viewLifecycleOwner) { setupBreweryList(it) }

        // Create and set up the RecyclerView adapter
        breweryListAdapter = BreweryListAdapter() // Pass initial empty list
        binding.breweryRecyclerView.adapter = breweryListAdapter
        binding.breweryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        binding.actionSearch.setOnClickListener {
            viewModel.searchAllBreweries()
        }
    }

    private fun setupBreweryList(breweryList: List<BreweryList>) {
        // Update the data in the RecyclerView adapter
        breweryListAdapter.submitList(breweryList)
    }

    private fun setupLoading(event: Event<Boolean?>) {
        event.contentIfNotHandled?.let {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }
}

class BreweryListAdapter : RecyclerView.Adapter<BreweryListAdapter.BreweryViewHolder>() {

    private var breweryList: List<BreweryList> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreweryViewHolder {
        val binding = BreweryListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreweryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreweryViewHolder, position: Int) {
        holder.bind(breweryList[position])
    }

    override fun getItemCount(): Int = breweryList.size

    fun submitList(newList: List<BreweryList>) {
        breweryList = newList
        notifyDataSetChanged()
    }

    inner class BreweryViewHolder(private val binding: BreweryListCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brewery: BreweryList) {
            val context = binding.root.context
            // Bind your data to the views in BreweryListCardBinding
            binding.breweryType.text =context.getString(
                R.string.brewery_type,
                brewery.brewery_type?.replaceFirstChar { it.uppercaseChar() } ?: ""
            )

            binding.breweryName.text = context.getString(
                R.string.brewery_name,
                brewery.name
            )
            binding.breweryLocation.text = context.getString(
                R.string.brewery_location,
                "${brewery.street}, " +
                        "\n" + "${brewery.city}, " +
                        "${brewery.state} " +
                        "\n" + "${brewery.postal_code}, " +
                        "${brewery.country}"
            )
        }
    }
}
