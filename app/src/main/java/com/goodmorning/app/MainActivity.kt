package com.goodmorning.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goodmorning.app.core.ui.theme.GoodMorningTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: GreetingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StartupConsole.log("MainActivity created")
        setContent {
            GoodMorningTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingScreen(viewModel = viewModel)
                }
            }
        }
        StartupConsole.log("Compose content attached")
    }

    override fun onStart() {
        super.onStart()
        StartupConsole.log("MainActivity started")
    }
}

@Composable
fun GreetingScreen(viewModel: GreetingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    GreetingContent(uiState = uiState)
}

@Composable
fun GreetingContent(uiState: GreetingUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        StartupConsolePanel()
        com.goodmorning.app.core.ui.components.Greeting(uiState.message)
    }
}
