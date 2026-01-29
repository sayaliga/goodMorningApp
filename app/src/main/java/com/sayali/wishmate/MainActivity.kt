package com.sayali.wishmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.wishmate.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sayali.wishmate.settings.LanguageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                // 🔥 Use LocalContext inside Compose
                val context = LocalContext.current

                // 🔹 One shared VM for the whole app
                val languageViewModel: LanguageViewModel = viewModel(
                    factory = LanguageViewModel.factory(context)
                )

                // Check login session
                val session = SessionManager(context)
                val startDestination = if (session.isLoggedIn()) "home" else "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("login") {
                        LoginScreen(navController, languageViewModel)
                    }

                    composable("otp/{verificationId}") { backStack ->
                        val verificationId = backStack.arguments?.getString("verificationId") ?: ""
                        OTPScreen(navController, verificationId)
                    }

                    composable("home") {
                        HomeScreen(navController, languageViewModel)
                    }

                    composable("language_selector") {
                        LanguageSelectorScreen(navController, languageViewModel)
                    }

                    composable(
                        route = "editor/{id}?tag={tag}"
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        val tag = backStackEntry.arguments?.getString("tag") ?: ""
                        EditorScreen(id = id, navController, searchTerm = tag)
                    }

                    composable(
                        route = "image_picker/{id}?tag={tag}",
                        arguments = listOf(
                            navArgument("id") { defaultValue = "en_morning_1" },
                            navArgument("tag") { defaultValue = "" }
                        )
                    ) { entry ->
                        val id = entry.arguments?.getString("id") ?: "en_morning_1"
                        val tag = entry.arguments?.getString("tag") ?: ""
                        ImagePickerScreen(id = id, navController = navController, searchTerm = tag)
                    }
                }
            }
        }
    }
}
