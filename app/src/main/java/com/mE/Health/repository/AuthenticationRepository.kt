package com.mE.Health.repository

import com.mE.Health.models.LoginRequest
import com.mE.Health.retrofit.APIService
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiService: APIService
) {

    suspend fun userLogin(request: LoginRequest) = apiService.userLogin(request)


}