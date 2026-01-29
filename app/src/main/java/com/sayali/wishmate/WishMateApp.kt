package com.sayali.wishmate

import android.app.Application
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig

class WishMateApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = PostHogAndroidConfig(
            apiKey = BuildConfig.POSTHOG_API_KEY,
            host = BuildConfig.POSTHOG_HOST
        ).apply {
            captureScreenViews = true
            sessionReplay = true
        }

        PostHogAndroid.setup(this, config)
    }
}
