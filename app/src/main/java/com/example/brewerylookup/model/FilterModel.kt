package com.example.brewerylookup.model

data class FilterModel(
    val breweryType: String?,
    val state: String?,
    val postalCode: String?,
    val city: String?
)