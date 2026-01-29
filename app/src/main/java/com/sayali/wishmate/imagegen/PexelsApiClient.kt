package com.sayali.wishmate.pexels

import android.net.Uri
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request

class PexelsApiClient(
    private val apiKey: String,
    private val http: OkHttpClient = OkHttpClient(),
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(PexelsSearchResponse::class.java)

    suspend fun searchPhotos(
        query: String,
        page: Int,
        perPage: Int = 30,              // Pexels allows up to 80
        orientation: String? = "portrait",
        size: String? = null,
        color: String? = null,
        locale: String? = null
    ): PexelsSearchResponse {
        val safePerPage = perPage.coerceIn(1, 80)

        val url = Uri.parse("https://api.pexels.com/v1/search")
            .buildUpon()
            .appendQueryParameter("query", query)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("per_page", safePerPage.toString())
            .apply {
                if (!orientation.isNullOrBlank()) appendQueryParameter("orientation", orientation)
                if (!size.isNullOrBlank()) appendQueryParameter("size", size)
                if (!color.isNullOrBlank()) appendQueryParameter("color", color)
                if (!locale.isNullOrBlank()) appendQueryParameter("locale", locale)
            }
            .build()
            .toString()

        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", apiKey)
            .get()
            .build()

        val resp = http.newCall(req).execute()
        val body = resp.body?.string()

        if (!resp.isSuccessful || body.isNullOrBlank()) {
            throw RuntimeException("Pexels API failed: HTTP ${resp.code} ${resp.message}")
        }

        return adapter.fromJson(body)
            ?: throw RuntimeException("Failed to parse Pexels response")
    }
}
