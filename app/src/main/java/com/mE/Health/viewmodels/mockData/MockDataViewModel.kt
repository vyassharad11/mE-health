package com.mE.Health.viewmodels.mockData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Patient
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.PractitionerBasicDetails
import com.mE.Health.data.model.PractitionerOrganization
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.data.model.Procedure
import com.mE.Health.data.repository.MockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MockDataViewModel @Inject constructor(
    private val repository: MockRepository
) : ViewModel() {

    private val _practitionerList = MutableLiveData<List<Practitioner>>()
    val practitionerList: LiveData<List<Practitioner>> = _practitionerList

    private val _appointmentList = MutableLiveData<List<Appointment>>()
    val appointmentList: LiveData<List<Appointment>> = _appointmentList

    private val _conditionList = MutableLiveData<List<Condition>>()
    val conditionList: LiveData<List<Condition>> = _conditionList

    private val _labList = MutableLiveData<List<DiagnosticReport>>()
    val labList: LiveData<List<DiagnosticReport>> = _labList

    private val _vitalsList = MutableLiveData<List<Observation>>()
    val vitalsList: LiveData<List<Observation>> = _vitalsList

    private val _medicationList = MutableLiveData<List<MedicationRequest>>()
    val medicationList: LiveData<List<MedicationRequest>> = _medicationList

    private val _visitList = MutableLiveData<List<Encounter>>()
    val visitList: LiveData<List<Encounter>> = _visitList

    private val _procedureList = MutableLiveData<List<Procedure>>()
    val procedureList: LiveData<List<Procedure>> = _procedureList

    private val _allergyList = MutableLiveData<List<AllergyIntolerance>>()
    val allergyList: LiveData<List<AllergyIntolerance>> = _allergyList

    private val _immunizationList = MutableLiveData<List<Immunization>>()
    val immunizationList: LiveData<List<Immunization>> = _immunizationList

    private val _claimList = MutableLiveData<List<Claim>>()
    val claimList: LiveData<List<Claim>> = _claimList

    private val _imagingList = MutableLiveData<List<Imaging>>()
    val imagingList: LiveData<List<Imaging>> = _imagingList

    private val _organizationList = MutableLiveData<List<PractitionerOrganizationWithDetails>>()
    val organizationList: LiveData<List<PractitionerOrganizationWithDetails>> = _organizationList

    private val _practAppointmentList = MutableLiveData<List<Appointment>>()
    val practAppointmentList: LiveData<List<Appointment>> = _practAppointmentList

    private val _practVisitList = MutableLiveData<List<Encounter>>()
    val practVisitList: LiveData<List<Encounter>> = _practVisitList

    private val _practitionerDetail = MutableLiveData<Practitioner>()
    val practitionerDetail: LiveData<Practitioner> = _practitionerDetail

    private val _patientDetail = MutableLiveData<Patient>()
    val patientDetail: LiveData<Patient> = _patientDetail

    private val _practitionerWithOrganization = MutableLiveData<PractitionerBasicDetails>()
    val practitionerWithOrganization: LiveData<PractitionerBasicDetails> = _practitionerWithOrganization


    init {
        insertPatients()
    }

    fun insertPatients() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMockData()

            getMockDataList()
        }
    }

    suspend fun getMockDataList() {
        _practitionerList.postValue(repository.getPractitionerList())
        _appointmentList.postValue(repository.getAppointmentList())
        _conditionList.postValue(repository.getConditionList())
        _labList.postValue(repository.getLabs())
        _vitalsList.postValue(repository.getVitals())
        _medicationList.postValue(repository.getMedicationList())
        _visitList.postValue(repository.getVisits())
        _procedureList.postValue(repository.getProcedure())
        _allergyList.postValue(repository.getAllergy())
        _immunizationList.postValue(repository.getImmunization())
        _claimList.postValue(repository.getClaim())
        _imagingList.postValue(repository.getImaging())
    }

    fun getOrganizationsByPractitionerId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _organizationList.postValue(repository.getOrganizationsByPractitionerId(id))
        }
    }

    fun getAppointmentsByPractitionerId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _practAppointmentList.postValue(repository.getAppointmentsByPractitionerId(id))
            _practVisitList.postValue(repository.getVisits(id))
        }
    }

    fun getPractitionerDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _practitionerDetail.postValue(repository.getPractitionerById(id))
        }
    }

    fun getPatientDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _patientDetail.postValue(repository.getPatientDetail(id))
        }
    }

    fun getPractitionerWithOrganization(encounterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _practitionerWithOrganization.postValue(repository.getPractitionerWithOrganization(encounterId))
        }
    }
}