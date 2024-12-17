package com.nacho.restaurantapplication.presentation.viewmodel.neworder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
import com.nacho.restaurantapplication.data.model.CartItem
import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.data.model.Dessert
import com.nacho.restaurantapplication.data.model.Dressing
import com.nacho.restaurantapplication.data.model.Drink
import com.nacho.restaurantapplication.data.model.Promotion
import com.nacho.restaurantapplication.data.model.Topping
import com.nacho.restaurantapplication.domain.usecase.home.user.GetUserCouponsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetAccompanimentsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetBurgersUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetDessertsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetDressingsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetDrinksUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetPromotionsUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.products.GetToppingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewOrderViewModel @Inject constructor(
    private val getBurgersUseCase: GetBurgersUseCase,
    private val getPromotionsUseCase: GetPromotionsUseCase,
    private val getDrinksUseCase: GetDrinksUseCase,
    private val getDessertsUseCase: GetDessertsUseCase,
    private val getAccompanimentsUseCase: GetAccompanimentsUseCase,
    private val getToppingsUseCase: GetToppingsUseCase,
    private val getDressingsUseCase: GetDressingsUseCase,
    private val getUserCouponsUseCase: GetUserCouponsUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //Region Toolbar
    private val _toolbarTitle = MutableLiveData<String>()
    val toolbarTitle: LiveData<String> get() = _toolbarTitle

    private val _toolbarShoppingCartVisibility = MutableLiveData<Boolean>()
    private val toolbarShoppingCartVisibility: LiveData<Boolean> get() = _toolbarShoppingCartVisibility

    private val _toolbarVisibilityState = MediatorLiveData<Boolean>()
    val toolbarVisibilityState: LiveData<Boolean> = _toolbarVisibilityState
    //End Region Toolbar

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

    //Region Accompaniments
    private val _accompaniments = MutableLiveData<List<Accompaniment>>()
    val accompaniments: LiveData<List<Accompaniment>> get() = _accompaniments
    //End Region Accompaniments

    //Region Toppings
    private val _toppings = MutableLiveData<List<Topping>>()
    val toppings: LiveData<List<Topping>> get() = _toppings
    //End Region Toppings

    //Region Dressing
    private val _dressings = MutableLiveData<List<Dressing>>()
    val dressings: LiveData<List<Dressing>> get() = _dressings
    //End Region Dressing

    //Region Assemble Burger
    private val _selectedItems = MutableLiveData<List<String>>()
    val selectedItems: LiveData<List<String>> get() = _selectedItems

    private val selectedToppings = mutableSetOf<String>()
    private val selectedDressings = mutableSetOf<String>()
    //End Region Assemble Burger

    //Region Shopping Cart
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _cartTotalPrice = MutableLiveData(0)
    val cartTotalPrice: LiveData<Int> = _cartTotalPrice

    private val _discountAmount = MutableLiveData(0)
    val discountAmount: LiveData<Int> = _discountAmount

    private val _userCoupons = MutableLiveData<List<Coupon>>()

    private val _finalTotalPrice = MutableLiveData(0)
    val finalTotalPrice: LiveData<Int> = _finalTotalPrice
    //End Region Shopping Cart

    //Tab Layout Region
    val selectedTabIndex = MutableLiveData<Int>()
    val sectionNames = listOf(BURGERS, PROMOTIONS, DRINKS, DESSERTS, ACCOMPANIMENTS)
    //End Tab Layout Region

    init {
        selectedTabIndex.value = 0

        _toolbarVisibilityState.addSource(cartItems) { cartItems ->
            val hasProducts = !cartItems.isNullOrEmpty()
            _toolbarVisibilityState.value = _toolbarShoppingCartVisibility.value == true && hasProducts
        }

        _toolbarVisibilityState.addSource(toolbarShoppingCartVisibility) { isVisible ->
            val hasProducts = !cartItems.value.isNullOrEmpty()
            _toolbarVisibilityState.value = isVisible && hasProducts
        }

        _cartItems.observeForever { items ->
            _cartTotalPrice.value = items.sumOf { it.price * it.quantity }
        }
    }

    //Region Tab Layout
    fun setSelectedTabIndex(index: Int) {
        selectedTabIndex.value = index
    }
    //End Region Tab Layout

    //Region Toolbar
    fun setToolbarTitle(title: String) = _toolbarTitle.postValue(title)

    fun setToolbarVisibility(visibility: Boolean) = _toolbarShoppingCartVisibility.postValue(visibility)
    //End Region Toolbar

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

    //Region Toppings And Dressing
    fun fetchToppingsAndDressings() {
        viewModelScope.launch {
            try {
                val toppings = getToppingsUseCase()
                val dressings = getDressingsUseCase()
                _toppings.postValue(toppings)
                _dressings.postValue(dressings)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
    //End Region Toppings And Dressing

    //Region Assemble Burger
    fun updateSelectedToppings(topping: String, isSelected: Boolean) {
        if (isSelected) selectedToppings.add(topping) else selectedToppings.remove(topping)
        updateSelectedItems()
    }

    fun updateSelectedDressings(dressing: String, isSelected: Boolean) {
        if (isSelected) selectedDressings.add(dressing) else selectedDressings.remove(dressing)
        updateSelectedItems()
    }

    private fun updateSelectedItems() {
        _selectedItems.value = (selectedToppings + selectedDressings).toList()
    }
    //End Region Assemble Burger

    //Region Shopping Cart
    fun addToCart(item: CartItem) {
        val currentCart = _cartItems.value.orEmpty().toMutableList()
        val existingItem = currentCart.find { it.title == item.title && it.type == item.type }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            currentCart[currentCart.indexOf(existingItem)] = updatedItem
        } else currentCart.add(item)

        _cartItems.value = currentCart
    }

    fun removeFromCart(item: CartItem) {
        val currentCart = _cartItems.value.orEmpty().toMutableList()
        currentCart.remove(item)
        _cartItems.value = currentCart
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        val currentCart = _cartItems.value.orEmpty().toMutableList()
        val index = currentCart.indexOfFirst { it.title == item.title && it.type == item.type }

        if (index >= 0) {
            currentCart[index] = currentCart[index].copy(quantity = newQuantity)
            _cartItems.value = currentCart
        }
    }

    fun getUserCoupons(uid: String) {
        viewModelScope.launch {
            try {
                val coupons = getUserCouponsUseCase(uid)
                _userCoupons.value = coupons
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    fun validateCoupon(inputCouponCode: String): Boolean {
        val coupon = _userCoupons.value?.find { it.code == inputCouponCode }
        return if (coupon != null) {
            _discountAmount.value = coupon.amount
            true
        } else {
            _discountAmount.value = 0
            false
        }
    }

    fun calculateTotalPrice() {
        val total = _cartTotalPrice.value ?: 0
        val discount = _discountAmount.value ?: 0
        val finalPrice = total - discount
        _finalTotalPrice.value = if (finalPrice < 0) 0 else finalPrice
    }
    //End Region Shopping Cart

}