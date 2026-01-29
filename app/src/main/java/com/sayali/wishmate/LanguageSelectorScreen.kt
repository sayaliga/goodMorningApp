package com.sayali.wishmate

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sayali.wishmate.settings.AppLanguage
import com.sayali.wishmate.settings.LanguageViewModel
import com.sayali.wishmate.theme.BgGradientEnd
import com.sayali.wishmate.theme.BgGradientStart
import com.sayali.wishmate.theme.MorningYellow
import com.sayali.wishmate.theme.NightBlue
import com.sayali.wishmate.theme.TextBrown

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectorScreen(
    navController: NavController,
    languageViewModel: LanguageViewModel
) {
    val languages = listOf(
        "English" to AppLanguage.ENGLISH,
        "Hindi" to AppLanguage.HINDI,
        "Marathi" to AppLanguage.MARATHI
    )

    val currentLang by languageViewModel.currentLanguage.collectAsState()

    ScreenScaffold(
        title = "Select Language",
        showTopBar = true
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BgGradientStart, BgGradientEnd)
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Choose your preferred language:",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBrown
                )

                Spacer(Modifier.height(16.dp))

                languages.forEach { (label, appLang) ->
                    val isSelected = currentLang == appLang

                    Button(
                        onClick = {
                            languageViewModel.setLanguage(appLang)
                            navController.navigate("home") {
                                popUpTo("language_selector") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MorningYellow else Color.White,
                            contentColor = TextBrown
                        ),
                        border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder
                    ) {
                        Text(
                            text = if (isSelected) "$label (Selected)" else label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
