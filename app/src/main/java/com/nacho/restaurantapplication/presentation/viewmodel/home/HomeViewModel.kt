package com.nacho.restaurantapplication.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.data.model.Store
import com.nacho.restaurantapplication.domain.usecase.home.stores.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _backInHome = MutableLiveData<Boolean>()
    val backInHome: LiveData<Boolean> = _backInHome

    private val _drawerOpen = MutableLiveData<Boolean>()
    val drawerOpen: LiveData<Boolean> = _drawerOpen

    //Region Stores
    private val _stores = MutableLiveData<List<Store>?>()
    val stores: LiveData<List<Store>?> get() = _stores
    //End Region Stores

    fun setDrawerOpen(open: Boolean) {
        _drawerOpen.value = open
    }

    fun handleBackNavigation(backPressed: Boolean) {
        _backInHome.value = backPressed
    }

    //Region Stores
    fun fetchStores() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val storeList = getStoresUseCase()
                _stores.value = storeList
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Stores

}