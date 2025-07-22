package com.mE.Health.di


import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.helper.NetworkStatusProvider
import com.mE.Health.data.repository.AssistRepository
import com.mE.Health.retrofit.LoginAPI
import com.mE.Health.retrofit.ProfileApiService
import com.mE.Health.retrofit.assist.AssistApiService
import com.mE.Health.utility.AppSession
import com.mE.Health.utility.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun providesRetrofit(baseUrl: String, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun providesFakerAPI(retrofit: Retrofit): LoginAPI {
        return retrofit.create(LoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AssistApiService =
        retrofit.create(AssistApiService::class.java)

    @Singleton
    @Provides
    fun provideAssistRepository(
        appSession: AppSession,
        apiService: AssistApiService,
        myItemDao: AssistDao,
        networkStatusProvider: NetworkStatusProvider
    ): AssistRepository {
        return AssistRepository(appSession, apiService, myItemDao, networkStatusProvider)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(): ProfileApiService {
        return Retrofit.Builder()
            .baseUrl("https://dev-admin.meinstein.ai/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApiService::class.java)
    }

}