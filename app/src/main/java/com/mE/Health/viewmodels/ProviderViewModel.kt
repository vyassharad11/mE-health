package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.ProviderDTO
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.data.repository.MockRepository
import com.mE.Health.models.CountryStateData
import com.mE.Health.models.ProviderData
import com.mE.Health.repository.ProviderRepository
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.AppSession
import com.mE.Health.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProviderViewModel @Inject constructor(
    private val repository: ProviderRepository,
    private val mockRepository: MockRepository,
    private val appSession: AppSession
) :
    ViewModel() {

    fun getAppSession(): AppSession {
        return appSession
    }

    private val _countryStateData = MutableLiveData<NetworkResult<CountryStateData>>()
    val stateListData: LiveData<NetworkResult<CountryStateData>>
        get() {
            return _countryStateData
        }

    private val _providerData = MutableLiveData<NetworkResult<ProviderData>>()
    val providerListData: LiveData<NetworkResult<ProviderData>>
        get() {
            return _providerData
        }

    private val _providerList = MutableLiveData<List<ProviderDTO>>()
    val providerList: LiveData<List<ProviderDTO>> = _providerList

    private val _userSavedFileList = MutableLiveData<List<UserSavedFile>>()
    val userSavedFileList: LiveData<List<UserSavedFile>> = _userSavedFileList


    fun stateList() {
        viewModelScope.launch(Dispatchers.IO) {
            _countryStateData.postValue(NetworkResult.Loading())
            repository.stateList().let  { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _countryStateData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _countryStateData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                } else {
                    _countryStateData.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }

    fun getUserSavedFileList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userSavedFileList.postValue(
                mockRepository.getUserSavedItemList(id, appSession.getUserId())
            )
        }
    }

    fun getProviderList() {
        viewModelScope.launch(Dispatchers.IO) {
            _providerList.postValue(mockRepository.getProviderItems())
        }
    }

    fun updateUserProviderAction(status: Boolean, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mockRepository.updateProviderStatus(status, id)
        }
    }

    fun insertFile(list: UserSavedFile) {
        viewModelScope.launch(Dispatchers.IO) {
            mockRepository.insertFile(list)
        }
    }
}