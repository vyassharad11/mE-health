package com.mE.Health.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mE.Health.data.SqlQueries
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Organization
import com.mE.Health.data.model.Patient
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.PractitionerBasicDetails
import com.mE.Health.data.model.PractitionerOrganization
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.data.model.Procedure

@Dao
interface MockDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergyIntolerance(data: List<AllergyIntolerance>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(data: List<Appointment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(data: List<Claim>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCondition(data: List<Condition>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosticReport(data: List<DiagnosticReport>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncounter(data: List<Encounter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImmunization(data: List<Immunization>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationRequest(data: List<MedicationRequest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(data: List<Observation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganization(data: List<Organization>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(data: List<Patient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractitioner(data: List<Practitioner>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractitionerOrganization(data: List<PractitionerOrganization>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProcedure(data: List<Procedure>)

    @Query("SELECT * FROM patient")
    fun getAll(): List<Patient>

    @Query("SELECT * FROM practitioner")
    fun getPractitionerList(): List<Practitioner>?

    @Query("SELECT * FROM practitioner WHERE id = :id")
    fun getPractitionerById(id: String): Practitioner?

    @Query("SELECT * FROM appointment")
    fun getAppointmentList(): List<Appointment>?

    @Query("SELECT * FROM condition")
    fun getConditionList(): List<Condition>?

    @Query("SELECT * FROM diagnostic_report")
    fun getLabs(): List<DiagnosticReport>?

    @Query("SELECT * FROM observation")
    fun getVitals(): List<Observation>?

    @Query("SELECT * FROM medication_request")
    fun getMedicationList(): List<MedicationRequest>?

    @Query("SELECT * FROM encounter")
    fun getVisits(): List<Encounter>?

    @Query("SELECT * FROM procedure")
    fun getProcedure(): List<Procedure>?

    @Query("SELECT * FROM allergy_intolerance")
    fun getAllergy(): List<AllergyIntolerance>?

    @Query("SELECT * FROM immunization")
    fun getImmunization(): List<Immunization>?

    @Query("SELECT * FROM claim")
    fun getClaim(): List<Claim>?

    @Query(
        """
        SELECT o.id, o.name, o.telecom, o.address
        FROM practitioner_organization po
        INNER JOIN organization o ON po.organizationId = o.id
        WHERE po.practitionerId = :practitionerId
    """
    )
    suspend fun getOrganizationsByPractitionerId(practitionerId: String): List<PractitionerOrganizationWithDetails>?

    @Query(SqlQueries.PRACTITIONER_WITH_ORGANIZATION)
    suspend fun getPractitionerWithOrganization(encounterId: String): PractitionerBasicDetails?

    @Query("SELECT * FROM appointment WHERE practitionerId = :id")
    fun getAppointmentsByPractitionerId(id: String): List<Appointment>?

    @Query("SELECT * FROM encounter WHERE practitionerId = :id")
    fun getVisits(id: String): List<Encounter>?

    @Query("SELECT * FROM patient WHERE id = :id")
    fun getPatientDetail(id: String): Patient?

}
