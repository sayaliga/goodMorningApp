package com.sayali.wishmate.imagegen

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object ImageRepository {
    private val client = OkHttpClient()

    // ⚠️ Ideally move this to BuildConfig later
    private const val UNSPLASH_KEY = "xBtDmiTg0yPnTSFlo55s_Wfb5PFOEqCYW5rAH6sXnt8"

    data class PagedResult(
        val urls: List<String>,
        val hasNext: Boolean
    )

    /**
     * ✅ Paginated Unsplash search using /search/photos
     */
    suspend fun fetchImageUrlsPaged(
        queries: List<String>,
        page: Int,
        perPage: Int = 30
    ): PagedResult = withContext(Dispatchers.IO) {
        val query = queries.joinToString(" ").trim().ifBlank { "nature" }

        val url = Uri.parse("https://api.unsplash.com/search/photos")
            .buildUpon()
            .appendQueryParameter("query", query)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("per_page", perPage.toString())
            .build()
            .toString()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Client-ID $UNSPLASH_KEY")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw RuntimeException("Unsplash error ${response.code}: $body")
            }

            val json = JSONObject(body)
            val results = json.optJSONArray("results")
                ?: return@withContext PagedResult(emptyList(), hasNext = false)

            val totalPages = json.optInt("total_pages", 0)

            val urls = mutableListOf<String>()
            for (i in 0 until results.length()) {
                val item = results.optJSONObject(i) ?: continue
                val imgUrl = item.optJSONObject("urls")?.optString("regular")
                if (!imgUrl.isNullOrBlank()) urls.add(imgUrl)
            }

            val hasNext = totalPages == 0 || page < totalPages
            PagedResult(urls = urls, hasNext = hasNext)
        }
    }
}
