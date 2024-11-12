package com.nacho.restaurantapplication.presentation.viewmodel.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.core.fragment.state.PaymentMethodViewState
import com.nacho.restaurantapplication.core.utils.Constants
import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.domain.model.UserCard
import com.nacho.restaurantapplication.domain.usecase.neworder.paymentMethods.AddUserCardUseCase
import com.nacho.restaurantapplication.domain.usecase.neworder.paymentMethods.GetUserCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val getUserCardsUseCase: GetUserCardUseCase,
    private val addUserCardUseCase: AddUserCardUseCase
) : ViewModel() {

    private val _uid = MutableLiveData<String?>()
    val uId: LiveData<String?> get() = _uid

    private val _viewStatePaymentMethod = MutableStateFlow(PaymentMethodViewState())
    val viewStatePaymentMethod: StateFlow<PaymentMethodViewState> get() = _viewStatePaymentMethod

    private val _userCards = MutableLiveData<List<Card>>()
    val userCards: LiveData<List<Card>> get() = _userCards

    private val _addCardSuccess = MutableLiveData(false)
    val addCardSuccess: LiveData<Boolean> = _addCardSuccess

    private val _toolbarVisible = MutableLiveData(true)
    val toolbarVisible: LiveData<Boolean> = _toolbarVisible

    fun setToolbarVisibility(visibility: Boolean) {
        _toolbarVisible.value = visibility
    }

    fun reset() {
        _addCardSuccess.postValue(false)
    }

    fun getUserId(userId: String) = _uid.postValue(userId)

    fun fetchUserCards(userId: String) {
        viewModelScope.launch {
            try {
                _userCards.value = getUserCardsUseCase(userId)
            } catch (e: Exception) {
                // Manejar el errores
            }
        }
    }

    fun addCard(uid: String, cardInformation: UserCard) {
        viewModelScope.launch {
            val success = addUserCardUseCase(uid, cardInformation)
            if (success) {
                _addCardSuccess.postValue(true)
            } else {
                // Manejar el error
            }
        }
    }

    fun onFieldsChangedPaymentMethod(userCard: UserCard, isCardValid: Boolean) {
        _viewStatePaymentMethod.value = userCard.toPaymentMethodViewState(isCardValid)
    }

    private fun isValidOrEmptyCardName(cardName: String): Boolean = cardName.length >= Constants.CARD_NAME_LENGTH || cardName.isEmpty()

    private fun isValidOrEmptyCardExpiration(cardDate: String): Boolean = cardDate.length >= Constants.EXPIRATION_CARD_LENGTH || cardDate.isEmpty()

    private fun isValidOrEmptyCardCvv(cardCvv: String): Boolean = cardCvv.length >= Constants.CVV_LENGTH || cardCvv.isEmpty()

    private fun UserCard.toPaymentMethodViewState(isCardValid: Boolean): PaymentMethodViewState {
        return PaymentMethodViewState(
            isValidCardNumber = isCardValid,
            isValidCardSince = isValidOrEmptyCardExpiration(cardSince),
            isValidCardUntil = isValidOrEmptyCardExpiration(cardUntil),
            isValidCardName = isValidOrEmptyCardName(cardName),
            isValidCardCvv = isValidOrEmptyCardCvv(cardCvv)
        )
    }

}