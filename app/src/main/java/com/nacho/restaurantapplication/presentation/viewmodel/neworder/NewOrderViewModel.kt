package com.nacho.restaurantapplication.presentation.viewmodel.neworder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nacho.restaurantapplication.core.utils.Constants.ACCOMPANIMENTS
import com.nacho.restaurantapplication.core.utils.Constants.BURGERS
import com.nacho.restaurantapplication.core.utils.Constants.DESSERTS
import com.nacho.restaurantapplication.core.utils.Constants.DRINKS
import com.nacho.restaurantapplication.core.utils.Constants.PROMOTIONS

class NewOrderViewModel : ViewModel() {

    val selectedTabIndex = MutableLiveData<Int>()
    val sectionNames = listOf(BURGERS, PROMOTIONS, DRINKS, DESSERTS, ACCOMPANIMENTS)

    init {
        selectedTabIndex.value = 0
    }

    fun setSelectedTabIndex(index: Int) {
        selectedTabIndex.value = index
    }

}