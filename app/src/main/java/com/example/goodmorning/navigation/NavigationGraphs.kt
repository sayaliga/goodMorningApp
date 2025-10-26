package com.example.goodmorning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.goodmorning.ui.screens.DashboardScreen
import com.example.goodmorning.ui.screens.OnboardingScreen
import com.example.goodmorning.ui.screens.ShareSheetScreen
import com.example.goodmorning.ui.screens.TemplateGalleryScreen

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    composable(AppDestination.Onboarding.route) {
        OnboardingScreen(
            onContinue = { navController.navigate(AppDestination.Dashboard.route) }
        )
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable(AppDestination.Dashboard.route) {
        DashboardScreen(
            onOpenTemplates = { navController.navigate(AppDestination.TemplateGallery.route) },
            onShare = { navController.navigate(AppDestination.ShareSheet.route) }
        )
    }
}

fun NavGraphBuilder.templateGalleryGraph(navController: NavController) {
    composable(AppDestination.TemplateGallery.route) {
        TemplateGalleryScreen(
            onTemplateSelected = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.shareSheetGraph(navController: NavController) {
    composable(AppDestination.ShareSheet.route) {
        ShareSheetScreen(
            onClose = { navController.popBackStack(AppDestination.Dashboard.route, inclusive = false) }
        )
    }
}
