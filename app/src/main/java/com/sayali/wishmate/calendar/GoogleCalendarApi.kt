package com.sayali.wishmate.calendar

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleCalendarApi {

    @GET("calendar/v3/calendars/{calendarId}/events")
    suspend fun listEvents(
        @Path("calendarId") calendarId: String,
        @Query("key") apiKey: String,
        @Query("timeMin") timeMin: String,
        @Query("timeMax") timeMax: String,
        @Query("singleEvents") singleEvents: Boolean = true,
        @Query("orderBy") orderBy: String = "startTime",
        @Query("maxResults") maxResults: Int = 250
    ): GoogleEventsResponse
}
