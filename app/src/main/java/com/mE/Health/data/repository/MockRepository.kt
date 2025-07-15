package com.mE.Health.data.repository

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mE.Health.data.dao.MockDataDao
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter

import com.mE.Health.data.model.ImagingStudyEntity
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Organization
import com.mE.Health.data.model.Patient
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.PractitionerOrganization
import com.mE.Health.data.model.Procedure
import com.mE.Health.data.model.AssistDetailEntity
import com.mE.Health.data.model.toAssistDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.dateToLocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockRepository @Inject constructor(
    private val mockDataDao: MockDataDao,
    val context: Application // <- for accessing assets
) {
    val gson = Gson()

    suspend inline fun <reified T> insertMockData(
        fileName: String, insert: suspend (List<T>) -> Unit
    ) {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }

        val type = object : TypeToken<List<T>>() {}.type
        val items: List<T> = gson.fromJson(json, type)

        insert(items)
    }

    suspend fun insertMockData() {
        //AllergyIntolerance
        insertMockData<AllergyIntolerance>(
            fileName = "allergy_intolerance.json"
        ) { data ->
            mockDataDao.insertAllergyIntolerance(data)
        }

        insertMockData<ImagingStudyEntity>(
            fileName = "imaging_study.json"
        ) { data ->
            mockDataDao.insertImagingStudy(data)
        }

        //Appointment
        insertMockData<Appointment>(
            fileName = "appointment.json"
        ) { data ->
            mockDataDao.insertAppointment(data)
        }

        //Claim
        insertMockData<Claim>(
            fileName = "claim.json"
        ) { data ->
            mockDataDao.insertClaim(data)
        }

        //Condition
        insertMockData<Condition>(
            fileName = "condition.json"
        ) { data ->
            mockDataDao.insertCondition(data)
        }

        //DiagnosticReport
        insertMockData<DiagnosticReport>(
            fileName = "diagnostic_report.json"
        ) { data ->
            mockDataDao.insertDiagnosticReport(data)
        }

        //Encounter
        insertMockData<Encounter>(
            fileName = "encounter.json"
        ) { data ->
            mockDataDao.insertEncounter(data)
        }

        //Immunization
        insertMockData<Immunization>(
            fileName = "immunization.json"
        ) { data ->
            mockDataDao.insertImmunization(data)
        }

        //MedicationRequest
        insertMockData<MedicationRequest>(
            fileName = "medication_request.json"
        ) { data ->
            mockDataDao.insertMedicationRequest(data)
        }

        //Observation
        insertMockData<Observation>(
            fileName = "observation.json"
        ) { data ->
            mockDataDao.insertObservation(data)
        }

        //Organization
        insertMockData<Organization>(
            fileName = "organization.json"
        ) { data ->
            mockDataDao.insertOrganization(data)
        }

        //Patient
        insertMockData<Patient>(
            fileName = "patient.json"
        ) { data ->
            mockDataDao.insertPatient(data)
        }

        //Practitioner
        insertMockData<Practitioner>(
            fileName = "practitioner.json"
        ) { data ->
            mockDataDao.insertPractitioner(data)
        }

        //PractitionerOrganization
        insertMockData<PractitionerOrganization>(
            fileName = "practitioner_organization.json"
        ) { data ->
            mockDataDao.insertPractitionerOrganization(data)
        }

        //Procedure
        insertMockData<Procedure>(
            fileName = "procedure.json"
        ) { data ->
            mockDataDao.insertProcedure(data)
        }

        //Imaging
        insertMockData<Imaging>(
            fileName = "imaging.json"
        ) { data ->
            mockDataDao.insertImaging(data)
        }
    }

    suspend fun getPractitionerList() = mockDataDao.getPractitionerList()
    suspend fun getPractitionerById(id: String) = mockDataDao.getPractitionerById(id)

    suspend fun getAppointmentList() = mockDataDao.getAppointmentList()
    suspend fun getConditionList() = mockDataDao.getConditionList()
    suspend fun getLabs() = mockDataDao.getLabs()

    suspend fun getVitals() = mockDataDao.getVitals()

    suspend fun getMedicationList() = mockDataDao.getMedicationList()

    suspend fun getVisits() = mockDataDao.getVisits()
    suspend fun getVisits(id: String) = mockDataDao.getVisits(id)
    suspend fun getPatientDetail(id: String) = mockDataDao.getPatientDetail(id)

    suspend fun getProcedure() = mockDataDao.getProcedure()

    suspend fun getAllergy() = mockDataDao.getAllergy()

    suspend fun getImmunization() = mockDataDao.getImmunization()

    suspend fun getClaim() = mockDataDao.getClaim()

    suspend fun getImaging() = mockDataDao.getImaging()

    suspend fun getOrganizationsByPractitionerId(practitionerId: String) =
        mockDataDao.getOrganizationsByPractitionerId(practitionerId)

    suspend fun getAppointmentsByPractitionerId(practitionerId: String) =
        mockDataDao.getAppointmentsByPractitionerId(practitionerId)

    suspend fun getPractitionerWithOrganization(encounterId: String) =
        mockDataDao.getPractitionerWithOrganization(encounterId)

    suspend fun insertAssistDetail(data: List<AssistDetailEntity>){
        mockDataDao.insertAssistDetail(data)
    }

    suspend fun getUnifiedHealthItems(
        startDate: String,
        endDate: String
    ): List<AssistDetailEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<AssistDetailEntity>()

        result += mockDataDao.getVitals().map { it.toAssistDetail() }
        result += mockDataDao.getLabs().map { it.toAssistDetail() }
        result += mockDataDao.getConditionList().map { it.toAssistDetail() }
        result += mockDataDao.getImagingStudyList().map { it.toAssistDetail() }
        result
            .filter {
                val date = it.date
                date != null && date.dateToLocalDate(Constants.YYYY_MM_DD_T_HH_MM_SS_Z)
                    .isAfter(startDate.dateToLocalDate(Constants.DD_MM_YYYY))
                        && date.dateToLocalDate(Constants.YYYY_MM_DD_T_HH_MM_SS_Z)
                    .isBefore(endDate.dateToLocalDate(Constants.DD_MM_YYYY))
            }
            .sortedByDescending { it.date }
    }

}
