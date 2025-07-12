package com.mE.Health.data.model

object DetailSingleton {

    var practitioner: Practitioner? = null
    var appointment: Appointment? = null
    var condition: Condition? = null
    var lab: DiagnosticReport? = null
    var vital: Observation? = null
    var medication: MedicationRequest? = null
    var visit: Encounter? = null
    var procedure: Procedure? = null
    var allergy: AllergyIntolerance? = null
    var immunization: Immunization? = null
    var claim: Claim? = null
    var imaging: Imaging? = null

}