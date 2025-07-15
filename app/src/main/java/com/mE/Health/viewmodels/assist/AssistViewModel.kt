package com.mE.Health.viewmodels.assist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mE.Health.data.helper.Resource
import com.mE.Health.data.model.assist.AssistItem
import com.mE.Health.data.repository.AssistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


@HiltViewModel
class AssistViewModel @Inject constructor(
    private val repository: AssistRepository
) : ViewModel() {

    private val _assistList = MutableStateFlow<Resource<List<AssistItem>>>(Resource.Loading())
    val assistList: StateFlow<Resource<List<AssistItem>>> = _assistList

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            repository.fetchAssistItems()
                .collect { result ->
                    _assistList.value = result
                }
        }
    }

    suspend fun fetchAssistData(): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val json = """{ "application": "Health" }"""
        val mediaType = "application/json".toMediaType()
        val requestBody: RequestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://dev-admin.meinstein.ai/core/assist-data-list/")
            .post(requestBody) // ← Important: use POST to send JSON
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        return@withContext if (response.isSuccessful) {
            response.body?.string()
        } else {
            null
        }
    }

}