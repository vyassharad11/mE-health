package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.models.CountryStateData
import com.mE.Health.models.ProviderData
import com.mE.Health.repository.ProviderRepository
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProviderViewModel @Inject constructor(
    private val repository: ProviderRepository
) :
    ViewModel() {

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


    fun stateList() {
        viewModelScope.launch(Dispatchers.IO) {
            _countryStateData.postValue(NetworkResult.Loading())
            repository.stateList().let { response ->
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

    fun providerList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.providerList().let { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _providerData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _providerData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                } else {
                    _providerData.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }
}