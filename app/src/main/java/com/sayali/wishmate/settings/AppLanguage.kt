package com.sayali.wishmate.settings

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "Hindi"),
    MARATHI("mr", "Marathi");

    companion object {
        fun fromCode(code: String?): AppLanguage? =
            values().find { it.code == code }
    }
}
