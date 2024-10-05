package com.nacho.restaurantapplication.presentation.viewmodel.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.core.Constants.MIN_NAME_LENGTH
import com.nacho.restaurantapplication.core.Constants.MIN_PASSWORD_LENGTH
import com.nacho.restaurantapplication.core.Constants.PHONE_LENGTH
import com.nacho.restaurantapplication.domain.model.UserSignup
import com.nacho.restaurantapplication.domain.usecase.login.CheckEmailExistsUseCase
import com.nacho.restaurantapplication.domain.usecase.login.CreateAccountUseCase
import com.nacho.restaurantapplication.domain.usecase.login.LoginUseCase
import com.nacho.restaurantapplication.domain.usecase.login.SendEmailVerificationUseCase
import com.nacho.restaurantapplication.domain.usecase.login.VerifyEmailUseCase
import com.nacho.restaurantapplication.presentation.fragment.login.state.SignUpViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val sendVerificationEmailUseCase: SendEmailVerificationUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val loginUseCase: LoginUseCase
    //private val saveAccountUseCase: SaveAccountUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(SignUpViewState())
    val viewState: StateFlow<SignUpViewState> get() = _viewState

    private val _isTermsAccepted = MutableLiveData(false)
    val isTermsAccepted: LiveData<Boolean> = _isTermsAccepted

    private val _showErrorFragment = MutableLiveData(false)
    val showErrorFragment: LiveData<Boolean> = _showErrorFragment

    private val _emailIsVerified = MutableLiveData<Boolean>()
    val emailIsVerified: LiveData<Boolean> = _emailIsVerified

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun onFieldsChanged(userSignup: UserSignup) {
        _viewState.value = userSignup.toSignupViewState()
    }

    fun onTermsAccepted(isAccepted: Boolean) {
        _isTermsAccepted.value = isAccepted
    }

    fun createAccount(userSignup: UserSignup) {
        viewModelScope.launch {
            val result = createAccountUseCase(userSignup.email, userSignup.password)
            if (result != null) {
                sendVerificationEmail()
            } else {
                _showErrorFragment.value = true
            }
        }
    }

    private suspend fun sendVerificationEmail() {
        val emailSent = sendVerificationEmailUseCase()
        if (emailSent) {
            checkEmailVerification()
        } else {
            _showErrorFragment.value = true
        }
    }

    private fun checkEmailVerification() {
        viewModelScope.launch {
            verifyEmailUseCase().collect { isVerified ->
                if (isVerified) {
                    _emailIsVerified.value = true
                }
            }
        }
    }

    fun checkEmailExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = checkEmailExistsUseCase(email)
            onResult(exists)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            _loginResult.value = result != null
        }
    }

    private fun isValidOrEmptyName(name: String): Boolean = name.length >= MIN_NAME_LENGTH || name.isEmpty()

    private fun isValidOrEmptyLastname(lastname: String): Boolean = lastname.length >= MIN_NAME_LENGTH || lastname.isEmpty()

    private fun isValidOrEmptyPhone(phone: String): Boolean = phone.length == PHONE_LENGTH || phone.isEmpty()

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()

    private fun isValidOrEmptyConfirmPassword(password: String, confirmPassword: String): Boolean =
        password == confirmPassword || confirmPassword.isEmpty()

    private fun UserSignup.toSignupViewState(): SignUpViewState {
        return SignUpViewState(
            isValidName = isValidOrEmptyName(name),
            isValidLastName = isValidOrEmptyLastname(lastname),
            isValidPhone = isValidOrEmptyPhone(phone),
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password),
            isValidConfirmPassword = isValidOrEmptyConfirmPassword(password, confirmPassword)
        )
    }

}
