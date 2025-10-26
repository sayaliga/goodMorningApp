package com.example.goodmorning.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.goodmorning.ui.screens.DashboardScreen
import com.example.goodmorning.ui.screens.OnboardingScreen
import com.example.goodmorning.ui.screens.ShareSheetScreen
import com.example.goodmorning.ui.screens.TemplateGalleryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodMorningApp() {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backstackEntry?.destination

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    val title = AppDestination.entries
                        .firstOrNull { dest ->
                            currentDestination?.hierarchy?.any { it.route == dest.route } == true
                        }
                        ?.let { stringResource(id = it.titleRes) }
                        ?: "Good Morning"
                    Text(text = title)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Onboarding.route,
            modifier = Modifier.fillMaxSize(),
            builder = {
                onboardingGraph(navController)
                dashboardGraph(navController)
                templateGalleryGraph(navController)
                shareSheetGraph(navController)
            }
        )
    }
}
