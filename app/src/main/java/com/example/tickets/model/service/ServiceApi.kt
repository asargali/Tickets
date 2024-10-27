package com.example.tickets.model.service

import com.example.tickets.model.entity.Offer
import retrofit2.Call
import retrofit2.http.GET

interface ServiceApi {
    @GET("fake-api-demo/offer_list")
    fun getFlights(): Call<List<Offer>>
}