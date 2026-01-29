package com.sayali.wishmate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import com.sayali.wishmate.theme.BgGradientEnd
import com.sayali.wishmate.theme.BgGradientStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String? = null,
    showTopBar: Boolean = true,
    onBack: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    useGradientBackground: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            // ⭐ APP ICON INSIDE TOPBAR
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "App Icon",
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 8.dp)
                            )

                            if (title != null) {
                                Text(text = title)
                            }
                        }
                    },
                    navigationIcon = {
                        if (onBack != null) {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, "Back")
                            }
                        }
                    },
                    actions = {
                        actions?.invoke()
                    }
                )
            }
        }
    ) { scaffoldPadding ->

        // ⭐ Apply Scaffold’s padding EXACTLY once
        val modifierWithPadding = Modifier
            .padding(scaffoldPadding)
            .fillMaxSize()

        // ⭐ Background (solid or gradient)
        val bgModifier = if (useGradientBackground) {
            modifierWithPadding.background(
                Brush.verticalGradient(
                    listOf(BgGradientStart, BgGradientEnd)
                )
            )
        } else modifierWithPadding

        Box(modifier = bgModifier) {
            content(scaffoldPadding)
        }
    }
}


