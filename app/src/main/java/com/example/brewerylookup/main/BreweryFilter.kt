package com.example.brewerylookup.main

import com.example.brewerylookup.model.BreweryList
import com.example.brewerylookup.model.FilterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreweryFilter @Inject constructor() {
    var breweryType: String? = null
    var breweryState: String? = null
    var breweryPostalCode: String? = null
    var breweryCity: String? = null
    var breweryName: String? = null

    fun matchesFilter(filter: BreweryList): Boolean {
        return if (isFiltering()) {
            val matchesBreweryType = (filter.brewery_type?.let { breweryType?.contains(it) } == true || breweryType?.isEmpty() == true)
            val matchesState = (filter.state?.let { breweryState?.contains(it) } == true) || (breweryState?.isEmpty() == true)
            val matchesPostalCode = (filter.postal_code?.let { breweryPostalCode?.contains(it) } == true) || (breweryPostalCode?.isEmpty() == true)
            val matchesCity = (filter.city?.let { breweryCity?.contains(it) } == true) || (breweryCity?.isEmpty() == true)
            val matchesBreweryName = (filter.name?.let { breweryName?.contains(it) } == true) || (breweryName?.isEmpty() == true)

            return matchesBreweryType && matchesState && matchesPostalCode && matchesCity && matchesBreweryName
        } else {
            true
        }
    }

    fun isFiltering(): Boolean {
        if (breweryType != null) return true
        if (breweryState != null) return true
        if (breweryPostalCode != null) return true
        if (breweryCity != null) return true
        if (breweryName != null) return true
        return false
    }
}