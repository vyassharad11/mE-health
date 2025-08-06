package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.data.repository.MockRepository
import com.mE.Health.repository.ProviderRepository
import com.mE.Health.utility.AppSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val mockRepository: MockRepository,
    private val appSession: AppSession
) :
    ViewModel() {

    fun getAppSession(): AppSession {
        return appSession
    }

    private val _userSavedFileList = MutableLiveData<List<UserSavedFile>>()
    val userSavedFileList: LiveData<List<UserSavedFile>> = _userSavedFileList

    fun getUserSavedFileList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userSavedFileList.postValue(
                mockRepository.getUserSavedItemList(id, appSession.getUserId())
            )
        }
    }

    fun insertFile(list: UserSavedFile) {
        viewModelScope.launch(Dispatchers.IO) {
            mockRepository.insertFile(list)
        }
    }

    fun deleteFile(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mockRepository.deleteFile(id, appSession.getUserId())
        }
    }
}