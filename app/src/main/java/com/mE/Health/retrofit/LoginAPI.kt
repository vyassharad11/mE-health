package com.mE.Health.retrofit

import com.mE.Health.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface LoginAPI {

    @GET("products")
    suspend fun getProducts() : Response<List<Product>>

//    @POST("user/login/")
//    suspend fun userLogin(@Body dataModal :LoginDTO) : Response<String>
}