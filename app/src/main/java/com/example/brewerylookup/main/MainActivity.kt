package com.example.brewerylookup.main

import android.os.Bundle
import com.example.brewerylookup.R
import com.example.inventory.common.BaseActivity
import com.example.inventory.utils.ActivityUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val fragment = MainFragment()
        ActivityUtils.addFragment(
            supportFragmentManager,
            fragment,
            R.id.fragment_container
        )
    }
}