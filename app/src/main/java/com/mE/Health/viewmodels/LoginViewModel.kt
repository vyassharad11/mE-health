package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.models.LoginRequest
import com.mE.Health.models.UserDataResponse
import com.mE.Health.repository.AuthenticationRepository
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.AppSession
import com.mE.Health.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val appSession: AppSession
) : ViewModel() {

    private val _loginStateData = MutableLiveData<NetworkResult<UserDataResponse>>()
    val loginStateData: LiveData<NetworkResult<UserDataResponse>>
        get() {
            return _loginStateData
        }

    private fun setAuthenticated(authenticated: UserDataResponse?) {
        appSession.setUserData(authenticated)
        if (authenticated?.data != null && authenticated.data!!.token != null) appSession.token =
            authenticated.data!!.token!!
    }


    fun userLogin(request: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginStateData.postValue(NetworkResult.Loading())
            repository.userLogin(request).let { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        appSession.setStringPreference(
                            Constants.USER_ID,
                            response.body()?.data?.userId ?: ""
                        )
                        setAuthenticated(response.body())
                        _loginStateData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _loginStateData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                } else {
                    _loginStateData.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }
}