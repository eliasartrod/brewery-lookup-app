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

    val loading: LiveData<Event<Boolean?>> = _loading
    val snackBar: LiveData<Event<SnackBarMessage>> = _snackBar
    val breweryList = _breweryList

    fun searchAllBreweries() {
        viewModelScope.launch {
            _loading.value = Event(true)
            val result = _breweryLookupRepository.searchAllBreweries(50)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _breweryList.value = it
                }
            } else {
                val error = result.exceptionOrNull()
                val message = SnackBarMessage(R.string.error_message_format)
                error?.localizedMessage?.let { message.addFormattedMessage(it) }
                _breweryList.value = listOf()
                _snackBar.value = Event(message)
            }
        }
        _loading.value = Event(false)
    }
}