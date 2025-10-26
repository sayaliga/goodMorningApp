package com.goodmorning.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.goodmorning.app.core.ui.theme.GoodMorningTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: GreetingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}

@Composable
fun GreetingScreen(viewModel: GreetingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    GreetingContent(uiState = uiState)
}

@Composable
fun GreetingContent(uiState: GreetingUiState) {
    com.goodmorning.app.core.ui.components.Greeting(uiState.message)
}
