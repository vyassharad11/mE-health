package com.mE.Health.data.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NetworkStatusProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    data class Offline<T>(val data: T) : Resource<T>()
}

fun <NetworkResponse, CachedData> NetworkBoundResource(
    isNetworkAvailable: Boolean,
    networkCall: suspend () -> NetworkResponse?,
    saveToDb: suspend (NetworkResponse) -> Unit,
    queryDb: suspend () -> CachedData
): Flow<Resource<CachedData>> = flow<Resource<CachedData>> {
    emit(Resource.Loading())

    val cached = queryDb()

    if (isNetworkAvailable) {
        try {
            val remote = networkCall()
            if (remote != null) {
                saveToDb(remote)
            }
            val updated = queryDb()
            emit(Resource.Success(updated))
        } catch (e: Exception) {
            if (cached is Collection<*> && cached.isNotEmpty()) {
                emit(Resource.Offline(cached))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Network error"))
            }
        }
    } else {
        if (cached is Collection<*> && cached.isNotEmpty()) {
            emit(Resource.Offline(cached))
        } else {
            emit(Resource.Error("No cached data found"))
        }
    }
}.flowOn(Dispatchers.IO)
