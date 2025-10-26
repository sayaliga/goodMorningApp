# Good Morning App

This repository contains an Android application scaffold for `com.goodmorning.app` built with Jetpack Compose and a modular MVVM architecture. It includes baseline integrations for Hilt dependency injection, Retrofit & Ktor networking, Room persistence, DataStore, Coil/Glide image loading, and Firebase Analytics/Crashlytics.

## Modules

- `app` – Application entry point with Hilt setup, product flavors (dev/beta/prod), and Compose UI screens.
- `core:data` – Data layer providing repositories, networking, persistence, and storage dependencies.
- `core:domain` – Domain layer with business logic abstractions and use cases.
- `core:ui` – Shared Compose UI components and theming utilities.

## Build configuration

- Kotlin DSL Gradle scripts with centralized version catalog (`gradle/libs.versions.toml`).
- Build variants include `dev`, `beta`, and `prod` flavors paired with `debug` and `release` build types.
- Baseline CI readiness through flavor-specific application IDs and Crashlytics/Analytics manifest placeholders.

## Getting started

1. Ensure you have the latest Android Studio, Android SDK (compile/target SDK 34), and JDK 17+ available on your machine.
2. Import the project into Android Studio or run Gradle tasks from the command line with the included wrapper (`./gradlew`).
   - The wrapper scripts will fetch the `gradle-wrapper.jar` from Gradle's distribution service on first use; make sure `curl`, `wget`, or PowerShell (on Windows) can reach the internet.
3. Allow the first Gradle sync/run to download the Android Gradle Plugin from Google's Maven repository.
4. Select the desired build variant (e.g., `devDebug`) and run on an emulator or device.

## Next steps

- Configure Retrofit/Ktor clients, Room database entities, and DataStore schemas to match your backend needs.
- Add Firebase configuration files (`google-services.json`) for analytics and crash reporting.
- Expand UI components and navigation flows according to product requirements.
