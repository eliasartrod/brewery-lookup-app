package com.example.brewerylookup.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewerylookup.R
import com.example.brewerylookup.data.BreweryLookupRepository
import com.example.brewerylookup.model.BreweryList
import com.example.inventory.common.AppPreferences
import com.example.inventory.common.Event
import com.example.inventory.common.SnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _appPreferences: AppPreferences,
    private val _breweryLookupRepository: BreweryLookupRepository
): ViewModel() {
    private val _loading = MutableLiveData<Event<Boolean?>>()
    private val _snackBar = MutableLiveData<Event<SnackBarMessage>>()
    private val _breweryList = MutableLiveData<List<BreweryList>>()
    private val _currentPageLiveData = MutableLiveData<Int>()

    val loading: LiveData<Event<Boolean?>> = _loading
    val snackBar: LiveData<Event<SnackBarMessage>> = _snackBar
    val currentPageLiveData: LiveData<Int> = _currentPageLiveData // Expose the LiveData
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

    fun searchByFilter(breweryType: String?, state: String?, postalCode: String?, city: String?, breweryName: String?) {
        viewModelScope.launch {
            _loading.value = Event(true)

            val result = _breweryLookupRepository.searchByFilter(breweryType, state, postalCode, city, breweryName)
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

    private fun updateCurrentPageLiveData() {
        _currentPageLiveData.value = currentPage
    }
}