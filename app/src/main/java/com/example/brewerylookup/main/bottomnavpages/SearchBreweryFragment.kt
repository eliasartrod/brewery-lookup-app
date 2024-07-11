package com.example.brewerylookup.main.bottomnavpages

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.BreweryListCardBinding
import com.example.brewerylookup.databinding.FragmentSearchBreweryBinding
import com.example.brewerylookup.main.BreweryFilterFragment
import com.example.brewerylookup.main.MainViewModel
import com.example.brewerylookup.model.BreweryList
import com.example.inventory.common.BaseFragment
import com.example.inventory.common.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBreweryFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBreweryBinding
    private lateinit var breweryListAdapter: SearchBreweryListAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBreweryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle(getString(R.string.brewery_search_page_title))

        showDialog()
        setupMenu()

        viewModel.loading.observe(viewLifecycleOwner) { setupLoading(it) }
        viewModel.snackBar.observe(viewLifecycleOwner) { showSnackBar(it) }
        viewModel.breweryList.observe(viewLifecycleOwner) { setupBreweryList(it) }

        breweryListAdapter = SearchBreweryListAdapter()
        binding.breweryRecyclerView.adapter = breweryListAdapter
        binding.breweryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun showDialog() {
        val dialog = BreweryFilterFragment()
        dialog.show(childFragmentManager, "filter")
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.filter_menu, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_filter -> {
                        val dialog = BreweryFilterFragment()
                        dialog.show(childFragmentManager, "filter")
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupBreweryList(breweryList: List<BreweryList>) {
        if (breweryList.isNotEmpty()) {
            binding.emptyList.visibility = View.INVISIBLE
        } else {
            binding.emptyList.visibility = View.VISIBLE
        }
        breweryListAdapter.submitList(breweryList)
    }

    private fun setupLoading(event: Event<Boolean?>) {
        event.contentIfNotHandled?.let {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }
}

class SearchBreweryListAdapter : RecyclerView.Adapter<SearchBreweryListAdapter.BreweryViewHolder>() {

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
        breweryList = emptyList()
        breweryList = newList
        notifyDataSetChanged()
    }

    inner class BreweryViewHolder(private val binding: BreweryListCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brewery: BreweryList) {
            val context = binding.root.context
            // Bind data to the views in BreweryListCardBinding
            if (brewery.brewery_type == null) {
                binding.breweryType.text = context.getString(R.string.empty_brewery_type)
            } else {
                binding.breweryType.text = context.getString(
                    R.string.brewery_type,
                    brewery.brewery_type.replaceFirstChar { it.uppercaseChar() }
                )
            }

            if (brewery.name == null) {
                binding.breweryName.text = context.getString(R.string.empty_brewery_name)
            } else {
                binding.breweryName.text = context.getString(
                    R.string.brewery_name,
                    brewery.name
                )
            }

            if (brewery.street == null || brewery.city == null ||
                brewery.state == null || brewery.postal_code == null ||
                brewery.country == null) {
                binding.breweryLocation.text = context.getString(R.string.empty_brewery_address)
            } else {
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
}
