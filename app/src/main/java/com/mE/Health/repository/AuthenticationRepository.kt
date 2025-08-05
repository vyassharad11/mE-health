package com.mE.Health.repository

import com.mE.Health.models.ContactUsRequest
import com.mE.Health.models.DeleteAccountRequest
import com.mE.Health.models.LoginRequest
import com.mE.Health.retrofit.APIService
import com.mE.Health.utility.Constants
import retrofit2.http.Query
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiService: APIService
) {

    suspend fun userLogin(request: LoginRequest) = apiService.userLogin(request)

    suspend fun getReasonList(
        authorization: String
    ) = apiService.getReasonList(
        authorization,
        Constants.source
    )

    suspend fun deleteUserAccount(
        authorization: String,
        request: DeleteAccountRequest
    ) = apiService.deleteUserAccount(
        authorization,
        request
    )

    suspend fun contactUs(
        authorization: String,
        request: ContactUsRequest
    ) = apiService.contactUs(
        authorization,
        request
    )
}