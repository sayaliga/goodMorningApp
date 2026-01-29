package com.sayali.wishmate.calendar

import com.google.gson.annotations.SerializedName

data class GoogleEventsResponse(
    @SerializedName("items") val items: List<GoogleEventItem> = emptyList()
)

data class GoogleEventItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("start") val start: GoogleEventTime? = null,
    @SerializedName("end") val end: GoogleEventTime? = null
)

data class GoogleEventTime(
    // All-day events use "date"; timed events use "dateTime"
    @SerializedName("date") val date: String? = null,
    @SerializedName("dateTime") val dateTime: String? = null
)

data class FestivalEvent(
    val id: String,
    val title: String,
    val date: String,              // yyyy-mm-dd (for all-day festivals)
    val tags: List<String> = emptyList()
)
