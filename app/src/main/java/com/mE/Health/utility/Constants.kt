package com.mE.Health.utility

import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    const val BASE_URL = "https://dev-admin.meinstein.ai/"
    var token = ""

    const val GET = "GET"
    const val POST = "POST"

    const val USER_ID = "USER_ID"
    const val DD_MM_YYYY = "dd-MM-yyyy"
    const val MM_DD_YYYY = "MM-dd-yyyy"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    const val USER_TYPE = "seller"
    const val PN_TYPE = "type"
    const val PN_SEARCH = "search"
    const val PN_NAME = "name"
    const val ALL = "Recent"
    const val RECENT = "Recent"
    const val CONNECTED = "Connected"
    const val STATE = "state"
    const val COUNTRY = "country"
    const val CONNECT = "connect"
    const val MY_CHART = "my_chart"
    const val READ_MORE = "read_more"
    const val DETAIL = "detail"
    const val PRACTITIONER = "Practitioner"
    const val MEDICATION = "Medication"
    const val VITAL = "Vital"
    const val LAB = "Lab"
    const val VISIT = "Visit"
    const val APPOINTMENT = "Appointment"
    const val CONNECTION_URL = "connection_url"
    const val FILE_DOCUMENT = "Document"
    const val FILE_VIDEO = "Video"
    const val FILE_IMAGE = "Image"
    const val FILE_PATH = "file_path"
    const val FILE_LENGTH = "file_length"
    const val FILE_NAME = "file_name"
    const val FILE_TYPE = "file_type"
    val DD_MM_YYYY_FORMATE = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    const val CODE = "code="
    const val BASE_URL_AUTH = "https://fhir.epic.com/interconnect-fhir-oauth/api/FHIR/R4/"
    const val CLIENT_ID = "58e04c49-b139-485c-8832-2b057c594329"
    const val REDIRECTION_URL = "smartFhirAuthApp://callback"

    const val SMART_CONFIGURATION = ".well-known/smart-configuration"

//    Code verifier : D6ZV0w-X6C1ZuOxB4GRPKDU9oMIvub9T0Xa67xDjsp4E8RRep6axGnCx2pd_ug9D
//    Code challange : BuDTvp2nl01lRuN7SnxKTQuoEArn3YjfkHz32CvY6xI

//    authURL = URL(string: "https://fhir.epic.com/interconnect-fhir-oauth/oauth2/authorize?client_id=\(Constants.API.clientID)&
//    response_type=code&redirect_uri=\(Constants.API.redirectURI)&
//    scope=openid profile patient/*.read patient/*.write offline_access&code_challenge=\(challenge)&code_challenge_method=S256")!

//    {fhir_base_url}/{smart_configuration}


//    const authUrl = `${authEndpoint}?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&state=${state}&aud=${aud}&code_challenge=${codeChallenge}&code_challenge_method=${codeChallengeMethod}`

}