package com.sayali.wishmate.calendar

interface FestivalCalendarRepository {
    suspend fun getFestivalsNextDays(days: Int = 7): List<FestivalEvent>
}
