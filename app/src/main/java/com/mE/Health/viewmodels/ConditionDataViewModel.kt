package com.mE.Health.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.repository.MockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionDataViewModel @Inject constructor(
    private val repository: MockRepository
) :
    ViewModel() {

    private val _observationByEncounterId = MutableLiveData<Pair<String, List<Observation>>>()
    val observationData: LiveData<Pair<String, List<Observation>>> = _observationByEncounterId

    private val _diagnosticReport = MutableLiveData<List<DiagnosticReport>>()
    val diagnosticReportData: LiveData<List<DiagnosticReport>> = _diagnosticReport

    private val _encounterData = MutableLiveData<List<Encounter>>()
    val encounterData: LiveData<List<Encounter>> = _encounterData

    private val _medicationData = MutableLiveData<List<MedicationRequest>>()
    val medicationData: LiveData<List<MedicationRequest>> = _medicationData


    private val _practitionerData = MutableLiveData<Pair<String, String>>()
    val practitionerData: LiveData<Pair<String, String>> = _practitionerData


    fun getObservationMapByEncounterId(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val organizationName = repository.getOrganizationNameByEncounterId(encounterId)
            val observations = repository.getObservationByEncounterId(encounterId)
            val result = Pair(
                first = organizationName ?: "Unknown Organization",
                second = observations
            )
            _observationByEncounterId.postValue(result)
        }
    }

    fun getLabsDataByEncounterId(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val labList = repository.getLabsDataByEncounterId(encounterId)
            val orgIds = labList.mapNotNull { it.organizationId }.distinct()
            val organizations = repository.getOrganizationsByIds(orgIds)
            val orgMap = organizations.associateBy { it.id }
            labList.forEach { item ->
                val orgName = orgMap[item.organizationId]?.name ?: "Unknown Organization"
                val orgAddress =
                    orgMap[item.organizationId]?.address ?: "Unknown Organization Address"
                item.organizationName = orgName
                item.organizationAddress = orgAddress
            }
            _diagnosticReport.postValue(labList)
        }
    }

    fun getVisitDataByEncounterId(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val visitList = repository.getVisitDataByEncounterId(encounterId)
            val orgIds = visitList.mapNotNull { it.organizationId }.distinct()
            val organizations = repository.getOrganizationsByIds(orgIds)
            val orgMap = organizations.associateBy { it.id }
            visitList.forEach { item ->
                val orgName = orgMap[item.organizationId]?.name ?: "Unknown Organization"
                val orgAddress =
                    orgMap[item.organizationId]?.address ?: "Unknown Organization Address"
                item.organizationName = orgName
                item.organizationAddress = orgAddress
            }
            _encounterData.postValue(visitList)
        }
    }

    fun getMedicationByEncounterId(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _medicationData.postValue(repository.getMedicationByEncounterId(encounterId))
        }
    }

    fun getPractitionerOrganizationName(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val encounter = repository.getVisitDataByEncounterId(encounterId).firstOrNull()

            if (encounter != null) {
                val practitionerId = encounter.practitionerId
                val organizationId = encounter.organizationId

                val practitionerName = practitionerId?.let { repository.getPractitionerName(it) }
                val organizationName = organizationId?.let { repository.getOrganizationName(it) }

                _practitionerData.postValue(
                    Pair(
                        practitionerName ?: "Unknown Practitioner",
                        organizationName ?: "Unknown Organization"
                    )
                )
            } else {
                _practitionerData.postValue(
                    Pair("Unknown Practitioner", "Unknown Organization")
                )
            }
        }
    }
}