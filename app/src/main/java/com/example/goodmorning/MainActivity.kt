package com.example.goodmorning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.goodmorning.navigation.GoodMorningApp
import com.example.goodmorning.ui.theme.GoodMorningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoodMorningTheme {
                Surface {
                    GoodMorningApp()
                }
            }
        }
    }
}
