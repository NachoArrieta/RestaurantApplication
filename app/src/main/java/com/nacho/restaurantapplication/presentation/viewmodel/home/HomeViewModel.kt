package com.nacho.restaurantapplication.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.restaurantapplication.core.utils.Constants.ADDRESS_LENGTH
import com.nacho.restaurantapplication.core.utils.Constants.MIN_NAME_LENGTH
import com.nacho.restaurantapplication.core.utils.Constants.PHONE_LENGTH
import com.nacho.restaurantapplication.data.model.News
import com.nacho.restaurantapplication.data.model.Store
import com.nacho.restaurantapplication.data.model.User
import com.nacho.restaurantapplication.domain.model.UserInformation
import com.nacho.restaurantapplication.domain.usecase.home.news.GetNewsUseCase
import com.nacho.restaurantapplication.domain.usecase.home.stores.GetStoresUseCase
import com.nacho.restaurantapplication.domain.usecase.home.user.GetUserInformationUseCase
import com.nacho.restaurantapplication.domain.usecase.home.user.UpdateUserInformationUseCase
import com.nacho.restaurantapplication.presentation.fragment.home.state.MyProfileViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInformationUseCase: GetUserInformationUseCase,
    private val updateUserInformationUseCase: UpdateUserInformationUseCase,
    private val getStoresUseCase: GetStoresUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    //States Region
    private val _viewState = MutableStateFlow(MyProfileViewState())
    val viewState: StateFlow<MyProfileViewState> get() = _viewState

    private val _hasChanges = MutableLiveData<Boolean>(false)
    val hasChanges: LiveData<Boolean> get() = _hasChanges

    private val _backInHome = MutableLiveData<Boolean>()
    val backInHome: LiveData<Boolean> = _backInHome

    private val _drawerOpen = MutableLiveData<Boolean>()
    val drawerOpen: LiveData<Boolean> = _drawerOpen
    //End States Region

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

    //Region User Information
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _userInformation = MutableLiveData<User?>()
    val userInformation: LiveData<User?> get() = _userInformation
    //End Region User Information

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

    //Region User Information
    fun setUserId(uid: String?) {
        _userId.postValue(uid)
    }

    fun fetchUserInformation(uid: String) {
        viewModelScope.launch {
            //Añadir Loading
            try {
                val userInfo = getUserInformationUseCase(uid)
                _userInformation.value = userInfo
                //Resetear el valor si se obtiene la informacion
            } catch (e: Exception) {
                //Mostrar error correspondiente si no se puede obtener la informacion
            } finally {
                //Quitar loading o shimmer
            }
        }
    }

    fun updateUserInformation(uid: String, userInformation: UserInformation) {
        viewModelScope.launch {
            val success = updateUserInformationUseCase(uid, userInformation)
            // Manejar el resultado de la actualización
            if (success) {
                // Actualización exitosa, notificar al usuario o actualizar la UI
            } else {
                // Manejar el error
            }
        }
    }
    //End Region User Information

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

    //Region Profile States
    fun onFieldsChanged(infoProfile: UserInformation) {
        _viewState.value = infoProfile.toMyProfileViewState()
    }

    private fun isValidOrEmptyName(name: String): Boolean = name.length >= MIN_NAME_LENGTH || name.isEmpty()

    private fun isValidOrEmptyLastname(lastname: String): Boolean = lastname.length >= MIN_NAME_LENGTH || lastname.isEmpty()

    private fun isValidOrEmptyPhone(phone: String): Boolean = phone.length == PHONE_LENGTH || phone.isEmpty()

    private fun isValidOrEmptyAddress(address: String): Boolean = address.length >= ADDRESS_LENGTH || address.isEmpty()

    private fun UserInformation.toMyProfileViewState(): MyProfileViewState {
        return MyProfileViewState(
            isValidName = isValidOrEmptyName(name),
            isValidLastName = isValidOrEmptyLastname(lastName),
            isValidPhone = isValidOrEmptyPhone(phone),
            isValidAddress = isValidOrEmptyAddress(address),)
    }
    //Region Profile States

}