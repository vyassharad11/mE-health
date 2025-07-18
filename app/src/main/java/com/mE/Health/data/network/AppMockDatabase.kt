package com.mE.Health.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mE.Health.data.Converters
import com.mE.Health.data.dao.MockDataDao
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.AssistDetailEntity
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.ImagingStudyEntity
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Organization
import com.mE.Health.data.model.Patient
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.PractitionerOrganization
import com.mE.Health.data.model.Procedure
import com.mE.Health.data.model.ProviderDTO

@Database(
    entities = [Patient::class, Practitioner::class, Organization::class, Appointment::class,
        Claim::class, Condition::class, DiagnosticReport::class, Encounter::class,
        Immunization::class, MedicationRequest::class, Observation::class, PractitionerOrganization::class,
        Procedure::class, AllergyIntolerance::class, ImagingStudyEntity::class, AssistDetailEntity::class,
        Imaging::class, ProviderDTO::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppMockDatabase : RoomDatabase() {
    abstract fun userDao(): MockDataDao
}
