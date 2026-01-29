package com.sayali.wishmate

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("goodmorning_prefs", Context.MODE_PRIVATE)

    fun setLoggedIn(phone: String) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_phone", phone)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun logout() {
        prefs.edit().clear().apply()
    }

    // ----- caption limit -----
    fun canGenerateCaption(): Boolean {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString("last_date", "")
        var count = prefs.getInt("caption_count", 0)

        if (lastDate != today) {
            count = 0
            prefs.edit().putString("last_date", today).putInt("caption_count", 0).apply()
        }

        return count < 5
    }

    fun incrementCaptionCount() {
        val count = prefs.getInt("caption_count", 0) + 1
        prefs.edit().putInt("caption_count", count).apply()
    }
}
