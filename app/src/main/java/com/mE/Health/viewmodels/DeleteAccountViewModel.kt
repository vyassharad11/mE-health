package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.AccountResponse
import com.mE.Health.data.model.ContactUsResponse
import com.mE.Health.data.model.DeleteAccountResponse
import com.mE.Health.models.ContactUsRequest
import com.mE.Health.models.DeleteAccountRequest
import com.mE.Health.repository.AuthenticationRepository
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.AppSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val appSession: AppSession
) : ViewModel() {

    fun getAppSession(): AppSession {
        return appSession
    }

    private val _reasonStateData = MutableLiveData<NetworkResult<AccountResponse>>()
    val reasonStateData: LiveData<NetworkResult<AccountResponse>>
        get() {
            return _reasonStateData
        }

    private val _deleteStateData = MutableLiveData<NetworkResult<DeleteAccountResponse>>()
    val deleteStateData: LiveData<NetworkResult<DeleteAccountResponse>>
        get() {
            return _deleteStateData
        }

    private val _contactUsStateData = MutableLiveData<NetworkResult<ContactUsResponse>>()
    val contactUsStateData: LiveData<NetworkResult<ContactUsResponse>>
        get() {
            return _contactUsStateData
        }

    fun getReasonData() {
        viewModelScope.launch(Dispatchers.IO) {
            _reasonStateData.postValue(NetworkResult.Loading())
            repository.getReasonList(appSession.token).let { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _reasonStateData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _reasonStateData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                } else {
                    _reasonStateData.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }

    fun deleteUserAccount(
        reason: String,
        source: String,
        satisfaction_rating: String,
        additional_feedback: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteStateData.postValue(NetworkResult.Loading())
            repository.deleteUserAccount(
                appSession.token, DeleteAccountRequest(reason = reason, source = source, satisfactionRating = satisfaction_rating, additionalFeedback = additional_feedback)
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _deleteStateData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _deleteStateData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                } else if (response.code() == 400) {
                    val errorMsg = response.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).optString("mE_text_res", "Bad request (400).")
                        } catch (e: Exception) {
                            "Bad request (400)."
                        }
                    } ?: "Bad request (400)."
                    _deleteStateData.postValue(NetworkResult.Error(errorMsg))
                } else {
                    _deleteStateData.postValue(NetworkResult.Error(response.body()?.mETextRes))
                }
            }
        }
    }

    fun makeContactUsApiCall(request: ContactUsRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _contactUsStateData.postValue(NetworkResult.Loading())
            repository.contactUs(appSession.token,request).let { response ->
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _contactUsStateData.postValue(NetworkResult.Success(response.body()))
                    } else {
                        _contactUsStateData.postValue(NetworkResult.Error("Something went wrong"))
                    }
                }  else if (response.code() == 400) {
                    val errorMsg = response.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).optString("mE_text_res", "Bad request (400).")
                        } catch (e: Exception) {
                            "Bad request (400)."
                        }
                    } ?: "Bad request (400)."
                    _contactUsStateData.postValue(NetworkResult.Error(errorMsg))
                } else {
                    _contactUsStateData.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }
}