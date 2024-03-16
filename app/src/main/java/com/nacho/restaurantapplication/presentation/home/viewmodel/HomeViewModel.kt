package com.nacho.restaurantapplication.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private val _navigateOnBack = MutableLiveData<Boolean>()
    val navigateOnBack: LiveData<Boolean> = _navigateOnBack












    fun handleBackNavigation(backPressed: Boolean) {
        _navigateOnBack.value = backPressed
    }

}