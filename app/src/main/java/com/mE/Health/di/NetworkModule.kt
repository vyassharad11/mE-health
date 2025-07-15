package com.mE.Health.di


import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.helper.NetworkStatusProvider
import com.mE.Health.data.repository.AssistRepository
import com.mE.Health.retrofit.LoginAPI
import com.mE.Health.retrofit.assist.AssistApiService
import com.mE.Health.utility.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun providesRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
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
        apiService: AssistApiService,
        myItemDao: AssistDao,
        networkStatusProvider: NetworkStatusProvider
    ): AssistRepository {
        return AssistRepository(apiService, myItemDao, networkStatusProvider)
    }
}