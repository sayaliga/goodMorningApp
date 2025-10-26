package com.example.goodmorning.navigation

import androidx.annotation.StringRes
import com.example.goodmorning.R

enum class AppDestination(val route: String, @StringRes val titleRes: Int) {
    Onboarding("onboarding", R.string.route_onboarding),
    Dashboard("dashboard", R.string.route_dashboard),
    TemplateGallery("template_gallery", R.string.route_template_gallery),
    ShareSheet("share_sheet", R.string.route_share_sheet)
}
