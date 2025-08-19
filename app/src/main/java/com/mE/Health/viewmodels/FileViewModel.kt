package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.Observation
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

    private val _observationByEncounterId = MutableLiveData<Pair<HashMap<String, String>, HashMap<String, String>>>()
    val observationData: LiveData<Pair<HashMap<String, String>, HashMap<String, String>>> = _observationByEncounterId

    private val _patientData = MutableLiveData<HashMap<String, String>>()
    val patientData: LiveData<HashMap<String, String>> = _patientData

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

    fun getOrganizationNameByEncounterId(imagingList: List<Imaging>, appointmentList: List<Appointment>) {
        val imagingMap = HashMap<String, String>()
        val appointmentMap = HashMap<String, String>()
        viewModelScope.launch(Dispatchers.IO) {
            for (item in imagingList) {
                imagingMap[item.encounterId!!] = mockRepository.getOrganizationNameByEncounterId(item.encounterId)!!
            }
            for (item in appointmentList) {
                appointmentMap[item.encounterId!!] = mockRepository.getOrganizationNameByEncounterId(item.encounterId)!!
            }
            _observationByEncounterId.postValue(Pair(imagingMap, appointmentMap))
        }
    }

    fun getPatientDetail(list: List<Immunization>) {
        val patientMap = HashMap<String, String>()
        viewModelScope.launch(Dispatchers.IO) {
            for (item in list) {
                patientMap[item.patientId!!] = mockRepository.getPatientDetail(item.patientId)?.name!!
            }
            _patientData.postValue(patientMap)
        }
    }
}