package com.mE.Health.repository

import com.mE.Health.data.dao.MockDataDao
import com.mE.Health.retrofit.APIService
import com.mE.Health.utility.AppSession
import javax.inject.Inject

class ProviderRepository @Inject constructor(
    private val apiService: APIService,
    private val appSession: AppSession,
    private val mockDataDao: MockDataDao
) {

    suspend fun stateList() = apiService.stateList(appSession.token)

    suspend fun providerStateList(search: String, state: String) =
        apiService.providerStateList(appSession.token, search, state)

    suspend fun providerCountryList(search: String, country: String) =
        apiService.providerCountryList(appSession.token, country, search)

    suspend fun providerList() {
        apiService.providerList(appSession.token).body().let {
            it?.data?.let { providerList ->
                mockDataDao.insertProviderData(providerList)
            } ?: run {
                throw Exception("Failed to fetch provider list")
            }
        }
    }
}