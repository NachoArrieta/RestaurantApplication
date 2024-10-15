package com.nacho.restaurantapplication.presentation.viewmodel.neworder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.core.utils.Constants.ACCOMPANIMENTS
import com.nacho.restaurantapplication.core.utils.Constants.BURGERS
import com.nacho.restaurantapplication.core.utils.Constants.DESSERTS
import com.nacho.restaurantapplication.core.utils.Constants.DRINKS
import com.nacho.restaurantapplication.core.utils.Constants.PROMOTIONS
import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.data.model.Drink
import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetAccompanimentsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetBurgersUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetDessertsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetDrinksUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetPromotionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewOrderViewModel @Inject constructor(
    private val getBurgersUseCase: GetBurgersUseCase,
    private val getPromotionsUseCase: GetPromotionsUseCase,
    private val getDrinksUseCase: GetDrinksUseCase,
    private val getDessertsUseCase: GetDessertsUseCase,
    private val getAccompanimentsUseCase: GetAccompanimentsUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //Region Burgers
    private val _burgers = MutableLiveData<Map<String, List<Burger>>>()
    val burgers: LiveData<Map<String, List<Burger>>> get() = _burgers
    //End Region Burgers

    //Region Promotions
    private val _promotions = MutableLiveData<List<Promotion>>()
    val promotions: LiveData<List<Promotion>> get() = _promotions
    //End Region Promotions

    //Region Drinks
    private val _drinks = MutableLiveData<Map<String, List<Drink>>>()
    val drinks: LiveData<Map<String, List<Drink>>> get() = _drinks
    //End Region Drinks

    //Region Desserts
    private val _desserts = MutableLiveData<Map<String, List<Dessert>>>()
    val desserts: LiveData<Map<String, List<Dessert>>> get() = _desserts
    //End Region Desserts

    // Region Accompaniments
    private val _accompaniments = MutableLiveData<List<Accompaniment>>()
    val accompaniments: LiveData<List<Accompaniment>> get() = _accompaniments
    //End Region Accompaniments

    //Tab Layout Region
    val selectedTabIndex = MutableLiveData<Int>()
    val sectionNames = listOf(BURGERS, PROMOTIONS, DRINKS, DESSERTS, ACCOMPANIMENTS)
    //End Tab Layout Region

    init {
        selectedTabIndex.value = 0
    }

    //Tab Layout Region
    fun setSelectedTabIndex(index: Int) {
        selectedTabIndex.value = index
    }
    //End Tab Layout Region

    //Region Burgers
    fun fetchBurgers() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val burgers = getBurgersUseCase()
                _burgers.postValue(burgers)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Burgers

    //Region Promotions
    fun fetchPromotions() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val promotions = getPromotionsUseCase()
                _promotions.postValue(promotions)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Promotions

    //Region Drinks
    fun fetchDrinks() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val drinks = getDrinksUseCase()
                _drinks.postValue(drinks)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Drinks

    //Region Desserts
    fun fetchDesserts() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val desserts = getDessertsUseCase()
                _desserts.postValue(desserts)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Desserts

    //Region Accompaniments
    fun fetchAccompaniments() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val accompaniments = getAccompanimentsUseCase()
                _accompaniments.postValue(accompaniments)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    //End Region Accompaniments

}