package com.example.goodmorning.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.goodmorning.ui.theme.LocalSpacing

@Composable
fun ShareSheetScreen(
    onClose: () -> Unit
) {
    val spacing = LocalSpacing.current
    val email = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Text(text = "Share campaign preview", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Recipient email") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "Good morning! Check out this personalized greeting template.",
            onValueChange = {},
            label = { Text("Message") },
            readOnly = true
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClose,
            enabled = email.value.isNotBlank()
        ) {
            Text("Send preview")
        }
    }
}
