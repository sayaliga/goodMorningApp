package com.sayali.wishmate.imagegen

import android.net.Uri
import com.sayali.wishmate.pexels.PexelsSearchResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object PexelsImageRepository {

    private val http = OkHttpClient()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(PexelsSearchResponse::class.java)

    /**
     * Pexels paginated search.
     * - queries: list of keywords/tags (we combine to a single query)
     * - page: 1-based
     * returns: urls + hasNext
     */
    suspend fun fetchImageUrlsPaged(
        queries: List<String>,
        page: Int,
        perPage: Int = 30
    ): PagedResult = withContext(Dispatchers.IO) {

        val safePerPage = perPage.coerceIn(1, 80)

        // Combine tags into one search query (works better than calling multiple times)
        val base = queries
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(" ")


        val url = Uri.parse("https://api.pexels.com/v1/search")
            .buildUpon()
            .appendQueryParameter("query", base)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("per_page", safePerPage.toString())
            .appendQueryParameter("orientation", "portrait") // greeting cards look better
            .build()
            .toString()

        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", "vugcmLURoU77fDx083jjWYwer3n9lAI5WDWhYjaF76fU8YwWX2LxLWqq")
            .get()
            .build()

        val resp = http.newCall(req).execute()
        val body = resp.body?.string()

        if (!resp.isSuccessful || body.isNullOrBlank()) {
            throw RuntimeException("Pexels API failed: HTTP ${resp.code} ${resp.message}")
        }

        val parsed = adapter.fromJson(body)
            ?: throw RuntimeException("Failed to parse Pexels response")

        val urls = parsed.photos.map { it.src.portrait } // keep using String list like your UI expects
        val hasNext = !parsed.next_page.isNullOrBlank()  // Pexels provides next_page only if next exists

        PagedResult(urls = urls, hasNext = hasNext)
    }

    data class PagedResult(
        val urls: List<String>,
        val hasNext: Boolean
    )
}
