package com.nacho.restaurantapplication.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private val _backInHome = MutableLiveData<Boolean>()
    val backInHome: LiveData<Boolean> = _backInHome

    private val _drawerOpen = MutableLiveData<Boolean>()
    val drawerOpen: LiveData<Boolean> = _drawerOpen

    fun setDrawerOpen(open: Boolean) {
        _drawerOpen.value = open
    }

    fun handleBackNavigation(backPressed: Boolean) {
        _backInHome.value = backPressed
    }

}