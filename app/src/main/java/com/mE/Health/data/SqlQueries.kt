package com.mE.Health.data

object SqlQueries {

    const val PRACTITIONER_WITH_ORGANIZATION = """
        SELECT 
      p.name AS practitionerName,
      p.specialty AS practitionerSpecialty,
      o.name AS organizationName
    FROM encounter e
    JOIN practitioner p ON e.practitionerId = p.id
    JOIN organization o ON e.organizationId = o.id
    WHERE e.id = :encounterId
    """

}