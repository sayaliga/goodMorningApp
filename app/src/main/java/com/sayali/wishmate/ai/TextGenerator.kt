package com.sayali.wishmate.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object TextGenerator {
    private val client = OkHttpClient()

    // Replace with your Firebase function URL
    private const val FUNCTION_URL =
        "https://us-central1-wishmatebackend.cloudfunctions.net/call_openai_api"

    suspend fun generateCaption(prompt: String, language: String = "English"): String? =
        withContext(Dispatchers.IO) {

            val json = JSONObject().apply {
                put("prompt", prompt)
                put("language", language)
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(FUNCTION_URL)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                response.body?.string()?.trim()
            }
        }
}
