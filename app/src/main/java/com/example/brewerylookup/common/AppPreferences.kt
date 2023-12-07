package com.example.inventory.common

import android.content.SharedPreferences
import com.google.gson.Gson

class AppPreferences(private val preferences: SharedPreferences, private val gson: Gson) {
    var isFiltered: Boolean
        get() = preferences.getBoolean(PREF_IS_FILTERED, false)
        set(value) = preferences.edit().putBoolean(PREF_IS_FILTERED, value).apply()

    var filteredBreweryType: String?
        get() = preferences.getString(PREF_FILTERED_BREWERY_TYPE, "") ?: ""
        set(value) = preferences.edit().putString(PREF_FILTERED_BREWERY_TYPE, value).apply()

    var filteredState: String?
        get() = preferences.getString(PREF_FILTERED_STATE, "") ?: ""
        set(value) = preferences.edit().putString(PREF_FILTERED_STATE, value).apply()

    var filteredPostalCode: String?
        get() = preferences.getString(PREF_FILTERED_POSTAL_CODE, "") ?: ""
        set(value) = preferences.edit().putString(PREF_FILTERED_POSTAL_CODE, value).apply()

    var filteredCity: String?
        get() = preferences.getString(PREF_FILTERED_CITY, "") ?: ""
        set(value) = preferences.edit().putString(PREF_FILTERED_CITY, value).apply()

    companion object {
        var PREF_IS_FILTERED = "pref.is.filtered"
        var PREF_FILTERED_BREWERY_TYPE = "pref.filtered.brewery.type"
        var PREF_FILTERED_STATE = "pref.filtered.state"
        var PREF_FILTERED_POSTAL_CODE = "pref.filtered.postal.code"
        var PREF_FILTERED_CITY = "pref.filtered.city"
    }
}
