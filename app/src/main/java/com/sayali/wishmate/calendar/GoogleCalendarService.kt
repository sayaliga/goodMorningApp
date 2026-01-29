package com.sayali.wishmate.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleCalendarService {

    private const val BASE_URL = "https://www.googleapis.com/"

    fun createApi(): GoogleCalendarApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleCalendarApi::class.java)
    }
}
