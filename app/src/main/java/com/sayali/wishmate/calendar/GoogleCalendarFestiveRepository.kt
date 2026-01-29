package com.sayali.wishmate.calendar

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri

class GoogleFestivalCalendarRepository(
    private val calendarIdRaw: String,
    private val apiKey: String
) {

    suspend fun getFestivalsNextDays(days: Int): List<FestivalEvent> {
        val (timeMin, timeMax) = computeTimeRange(days)

        // IMPORTANT: encode calendarId ONCE
        val calendarIdEncoded =
            URLEncoder.encode(calendarIdRaw, "UTF-8")

        val urlString = Uri.parse("https://www.googleapis.com/calendar/v3/calendars/$calendarIdEncoded/events")
            .buildUpon()
            .appendQueryParameter("key", apiKey)
            .appendQueryParameter("timeMin", timeMin)
            .appendQueryParameter("timeMax", timeMax)
            .appendQueryParameter("singleEvents", "true")
            .appendQueryParameter("orderBy", "startTime")
            .appendQueryParameter("maxResults", "250")
            .build()
            .toString()
        Log.d("FestivalHTTP", "GET $urlString")

        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "GET"
        conn.connectTimeout = 15_000
        conn.readTimeout = 15_000

        try {
            val responseCode = conn.responseCode
            Log.d("FestivalHTTP", "Response code = $responseCode")

            val stream =
                if (responseCode in 200..299) conn.inputStream
                else conn.errorStream

            val body = stream.bufferedReader().use { it.readText() }
            Log.d("FestivalHTTP", "Response body = $body")

            if (responseCode !in 200..299) {
                throw RuntimeException("HTTP $responseCode: $body")
            }

            return parseEvents(body)
        } finally {
            conn.disconnect()
        }
    }

    private fun parseEvents(json: String): List<FestivalEvent> {
        val root = JSONObject(json)
        val items = root.optJSONArray("items") ?: return emptyList()

        val result = mutableListOf<FestivalEvent>()

        for (i in 0 until items.length()) {
            val obj = items.getJSONObject(i)

            val title = obj.optString("summary", "")
            val start = obj.optJSONObject("start")
            val date = start?.optString("date") // all-day events only

            if (title.isNotBlank() && date != null) {
                result.add(
                    FestivalEvent(
                        id = obj.optString("id", "$title-$date"),
                        title = title,
                        date = date,
                        tags = emptyList()
                    )
                )
            }
        }
        return result
    }

    private fun computeTimeRange(days: Int): Pair<String, String> {
        val tz = TimeZone.getTimeZone("Asia/Kolkata")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
        sdf.timeZone = tz

        val start = Calendar.getInstance(tz)
        val end = Calendar.getInstance(tz).apply {
            add(Calendar.DAY_OF_YEAR, days)
        }

        return sdf.format(start.time) to sdf.format(end.time)
    }
}
