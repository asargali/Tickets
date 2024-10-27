package com.example.tickets.model.network

import com.example.tickets.model.service.ServiceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://my-json-server.typicode.com/estharossa/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ServiceApi by lazy {
        retrofit.create(ServiceApi::class.java)
    }
}