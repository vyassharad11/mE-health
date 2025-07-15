package com.mE.Health.data.repository

import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.helper.NetworkBoundResource
import com.mE.Health.data.helper.NetworkStatusProvider
import com.mE.Health.data.helper.Resource
import com.mE.Health.data.model.assist.AssistItem
import com.mE.Health.retrofit.assist.AssistApiService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AssistRepository @Inject constructor(
    private val apiService: AssistApiService,
    private val myItemDao: AssistDao,
    private val networkStatusProvider: NetworkStatusProvider
) {
    fun fetchAssistItems(): Flow<Resource<List<AssistItem>>> {
        val body = mapOf("application" to "Health")
        return NetworkBoundResource(
            isNetworkAvailable = networkStatusProvider.isNetworkAvailable(),
            networkCall = { apiService.getAssistDataList(body)?.data },
            saveToDb = { remoteList ->
                val entities = remoteList
                    .filter { it.application == "Health" }
                    .map {
                        AssistItem(
                            id = it.id,
                            application = it.application,
                            category = it.category,
                            item = it.item,
                            default_activation_for_assist = it.default_activation_for_assist,
                            frequency = it.frequency,
                            frequency_in_days = it.frequency_in_days
                        )
                    }
                myItemDao.insertAll(entities)
            },
            queryDb = { myItemDao.getAllItems() }
        )
    }

}