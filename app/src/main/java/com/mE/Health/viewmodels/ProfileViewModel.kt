package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.models.ProfileData
import com.mE.Health.retrofit.ProfileApiService
import com.mE.Health.utility.AppSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ProfileApiService,
    private val appSession: AppSession
) : ViewModel() {

    private val _profile = MutableLiveData<ProfileData>()
    val profile: LiveData<ProfileData> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = api.getProfile(
                    token = appSession.token,
                    userId = mapOf("user_id" to appSession.getUserData()?.data?.userId!!)
                )
                if (response.status == 200) {
                    _profile.postValue(response.data)
                } else {
                    _error.postValue(response.message)
                }
            } catch (e: Exception) {
                _error.postValue("Failed: ${e.localizedMessage}")
            }
        }
    }
}
