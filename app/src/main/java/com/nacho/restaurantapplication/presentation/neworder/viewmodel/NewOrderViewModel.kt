package com.nacho.restaurantapplication.presentation.neworder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewOrderViewModel : ViewModel() {

    val selectedTabIndex = MutableLiveData<Int>()
    val sectionNames = listOf(
        "Hamburguesas",
        "Promociones",
        "Bebidas",
        "Acompañamientos",
        "Postres"
    )

    init {
        selectedTabIndex.value = 0
    }

    fun setSelectedTabIndex(index: Int) {
        selectedTabIndex.value = index
    }

}