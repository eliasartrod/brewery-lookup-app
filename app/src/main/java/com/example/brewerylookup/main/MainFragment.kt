package com.example.brewerylookup.main

import android.os.Bundle
import android.view.*
import com.example.brewerylookup.R
import com.example.brewerylookup.databinding.FragmentMainBinding
import com.example.brewerylookup.main.bottomnavpages.FullBreweryListFragment
import com.example.brewerylookup.main.bottomnavpages.MapsFragment
import com.example.brewerylookup.main.bottomnavpages.SearchBreweryFragment
import com.example.inventory.common.BaseFragment
import com.example.inventory.utils.ActivityUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding

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

        setupDefaultPage()
        setupBottomNavView()

    }


    private fun setupDefaultPage() {
        ActivityUtils.replaceFragment(
            childFragmentManager,
            FullBreweryListFragment(),
            R.id.subFragmentContainer
        )
    }

    private fun setupBottomNavView() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.brewery_list -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        FullBreweryListFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                R.id.brewery_search -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        SearchBreweryFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                R.id.brewery_map_it -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        MapsFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                else -> false
            }
        }
    }
}
