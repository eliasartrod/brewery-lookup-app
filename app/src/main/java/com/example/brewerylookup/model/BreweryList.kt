package com.example.brewerylookup.model

import com.example.brewerylookup.network.model.NetworkBreweryListResponse

class BreweryList(
    val id: String?,
    val name: String?,
    val brewery_type: String?,
    val address_1: String?,
    val address_2: Any?,
    val address_3: Any?,
    val city: String?,
    val state_province: String?,
    val postal_code: String?,
    val country: String?,
    val longitude: String?,
    val latitude: String?,
    val phone: String?,
    val website_url: String?,
    val state: String?,
    val street: String?
) {
    companion object {
        fun fromNetwork(networkBreweryListResponse: List<NetworkBreweryListResponse>): List<BreweryList> {
            return networkBreweryListResponse.map { brewery ->
                BreweryList(
                    id = brewery.id,
                    name = brewery.name,
                    brewery_type = brewery.brewery_type,
                    address_1 = brewery.address_1,
                    address_2 = brewery.address_2,
                    address_3 = brewery.address_3,
                    city = brewery.city,
                    state_province = brewery.state_province,
                    postal_code = brewery.postal_code,
                    country = brewery.country,
                    longitude = brewery.longitude,
                    latitude = brewery.latitude,
                    phone = brewery.phone,
                    website_url = brewery.website_url,
                    state = brewery.state,
                    street = brewery.street
                )
            }
        }
    }
}