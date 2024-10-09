package com.nacho.restaurantapplication.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.data.model.News
import com.nacho.restaurantapplication.data.model.Store
import com.nacho.restaurantapplication.domain.usecase.home.news.GetNewsUseCase
import com.nacho.restaurantapplication.domain.usecase.home.stores.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _backInHome = MutableLiveData<Boolean>()
    val backInHome: LiveData<Boolean> = _backInHome

    private val _drawerOpen = MutableLiveData<Boolean>()
    val drawerOpen: LiveData<Boolean> = _drawerOpen

    //Loading Region
    private val _loadingStores = MutableLiveData<Boolean>()
    val loadingStores: LiveData<Boolean> = _loadingStores

    private val _loadingNews = MutableLiveData<Boolean>()
    val loadingNews: LiveData<Boolean> = _loadingNews
    //End Loading Region

    //Region News
    private val _news = MutableLiveData<List<News>?>()
    val news: LiveData<List<News>?> get() = _news
    //End Region News

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

    //Region News
    fun fetchNews() {
        viewModelScope.launch {
            _loadingNews.postValue(true)
            try {
                val news = getNewsUseCase()
                _news.value = news
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _loadingNews.postValue(false)
            }
        }
    }
    //End Region News

    //Region Stores
    fun fetchStores() {
        viewModelScope.launch {
            _loadingStores.postValue(true)
            try {
                val storeList = getStoresUseCase()
                _stores.value = storeList
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _loadingStores.postValue(false)
            }
        }
    }
    //End Region Stores

}