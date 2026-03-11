import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sayali.wishmate"
    val keystoreProps = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) load(file.inputStream())
    }
    val googleCalendarApiKey: String =
        keystoreProps.getProperty("GOOGLE_CALENDAR_API_KEY") ?: ""

    val festivalCalendarId: String =
        keystoreProps.getProperty("FESTIVAL_CALENDAR_ID") ?: ""

    val posthogApiKey: String =
        keystoreProps.getProperty("POSTHOG_API_KEY") ?: ""

    val posthogHost: String =
        keystoreProps.getProperty("POSTHOG_HOST") ?: ""


    signingConfigs {
        create("release") {
            storeFile =
                file("/Users/sayali/AndroidStudioProjects/goodmorningApp2/keystore/release.keystore")
            storePassword = "Saug#0494"
            keyAlias = "goodmorning"
            keyPassword = "Saug#0494"
        }
    }
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.sayali.wishmate"
        minSdk = 24
        targetSdk = 36
        versionCode = 15
        versionName = "15.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "GOOGLE_CALENDAR_API_KEY",
            "\"$googleCalendarApiKey\""
        )

        buildConfigField(
            "String",
            "FESTIVAL_CALENDAR_ID",
            "\"$festivalCalendarId\""
        )

        buildConfigField(
            "String",
            "POSTHOG_API_KEY",
            "\"$posthogApiKey\""
        )

        buildConfigField(
            "String",
            "POSTHOG_HOST",
            "\"$posthogHost\""
        )
    }

    buildTypes {
        debug {
            // This is actually true by default, but you can be explicit:
            isDebuggable = true
            // make sure debug is NOT using the release signing config
            // (it will use the default debug keystore)
            applicationIdSuffix = ".dev"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20240303")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-bom:33.3.0") // optional but recommended
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.posthog:posthog-android:3.+")
}