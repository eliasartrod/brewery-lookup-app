package com.example.brewerylookup.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewerylookup.R
import com.example.brewerylookup.data.BreweryLookupRepository
import com.example.brewerylookup.model.BreweryList
import com.example.brewerylookup.model.Directions
import com.example.inventory.common.AppPreferences
import com.example.inventory.common.Event
import com.example.inventory.common.SnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _appPreferences: AppPreferences,
    private val _breweryLookupRepository: BreweryLookupRepository,
    val filter: BreweryFilter
): ViewModel() {
    private val _loading = MutableLiveData<Event<Boolean?>>()
    private val _snackBar = MutableLiveData<Event<SnackBarMessage>>()
    private val _breweryList = MutableLiveData<List<BreweryList>>()
    private val _currentPageLiveData = MutableLiveData<Int>()
    private val _directionsResult = MutableLiveData<Directions?>()
    private var _apiKey: String? = null

    val loading: LiveData<Event<Boolean?>> = _loading
    val snackBar: LiveData<Event<SnackBarMessage>> = _snackBar
    val currentPageLiveData: LiveData<Int> = _currentPageLiveData
    val directionsResult: LiveData<Directions?> = _directionsResult
    val breweryList = _breweryList

    private var currentPage = 1
    private val itemsPerPage = 10

    fun searchAllBreweries(pageNumber: Int, amountPerPage: Int) {
        viewModelScope.launch {
            _loading.value = Event(true)
            val result = _breweryLookupRepository.searchAllBreweries(pageNumber,amountPerPage)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _breweryList.value = emptyList()
                    _breweryList.value = it
                }
            } else {
                val error = result.exceptionOrNull()
                val message = SnackBarMessage(R.string.error_message_format)
                error?.localizedMessage?.let { message.addFormattedMessage(it) }
                _breweryList.value = listOf()
                _snackBar.value = Event(message)
            }
            _loading.value = Event(false)
        }
    }

    fun searchByFilter(breweryFilter: BreweryFilter) {
        viewModelScope.launch {
            _loading.value = Event(true)
            val result = _breweryLookupRepository.searchByFilter(
                breweryFilter.breweryType,
                breweryFilter.breweryState,
                breweryFilter.breweryPostalCode,
                breweryFilter.breweryCity,
                breweryFilter.breweryName
            )
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _breweryList.value = emptyList()
                    updateFilter(it)
                }
            } else {
                val error = result.exceptionOrNull()
                val message = SnackBarMessage(R.string.error_message_format)
                error?.localizedMessage?.let { message.addFormattedMessage(it) }
                _breweryList.value = listOf()
                _snackBar.value = Event(message)
            }
            _loading.value = Event(false)
        }
    }

    fun loadNextTenBreweries() {
        viewModelScope.launch {
            _loading.value = Event(true)
            val result = _breweryLookupRepository.searchAllBreweries(currentPage + 1, itemsPerPage)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _breweryList.value = emptyList()
                    _breweryList.value = it
                    currentPage++
                    updateCurrentPageLiveData()
                }
            } else {
                val error = result.exceptionOrNull()
                val message = SnackBarMessage(R.string.error_message_format)
                error?.localizedMessage?.let { message.addFormattedMessage(it) }
                _snackBar.value = Event(message)
            }
            _loading.value = Event(false)
        }
    }

    fun loadPreviousTenBreweries() {
        if (currentPage > 1) {
            viewModelScope.launch {
                _loading.value = Event(true)
                val result = _breweryLookupRepository.searchAllBreweries(currentPage - 1, itemsPerPage)
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        _breweryList.value = emptyList()
                        _breweryList.value = it
                        currentPage--
                        updateCurrentPageLiveData()
                    }
                } else {
                    val error = result.exceptionOrNull()
                    val message = SnackBarMessage(R.string.error_message_format)
                    error?.localizedMessage?.let { message.addFormattedMessage(it) }
                    _snackBar.value = Event(message)
                }
                _loading.value = Event(false)
            }
        }
    }

    private fun updateFilter(breweries: List<BreweryList>) {
        val filteredBreweries = breweries.filter { filter.matchesFilter(it) }
        Timber.d("List Size: ", filteredBreweries.size)
        _breweryList.value = filteredBreweries
    }

    fun setApiKey() {
        _appPreferences.apiKey = "AIzaSyA5e_5QRVEqbL3S0PiN9hnBeY0bPO2JGJQ"
    }

    fun searchDirections(startingAddress: String, destinationAddress: String) {
        viewModelScope.launch {
            _loading.value = Event(true)
            _apiKey = "AIzaSyA5e_5QRVEqbL3S0PiN9hnBeY0bPO2JGJQ"
            val result = _breweryLookupRepository.getDirections(startingAddress, destinationAddress, _apiKey!!)
            if (result.isSuccess) {
                result.getOrNull().let {
                    _directionsResult.value = it
                }
            } else {
                _directionsResult.value = null
                val error = result.exceptionOrNull()
                val message = SnackBarMessage(R.string.error_message_format)
                error?.localizedMessage?.let { message.addFormattedMessage(it) }
                _snackBar.value = Event(message)
            }
            _loading.value = Event(false)
        }
    }

    private fun updateCurrentPageLiveData() {
        _currentPageLiveData.value = currentPage
    }
}