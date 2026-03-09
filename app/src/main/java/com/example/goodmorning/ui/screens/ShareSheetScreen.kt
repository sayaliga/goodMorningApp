package com.example.goodmorning.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.goodmorning.content.samplePromptTemplates
import com.example.goodmorning.ui.theme.LocalSpacing

@Composable
fun ShareSheetScreen(
    onClose: () -> Unit
) {
    val spacing = LocalSpacing.current
    val email = remember { mutableStateOf("") }
    val selectedTemplate = remember { samplePromptTemplates.first() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Text(
            text = "Share campaign preview",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Prompt tone",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = selectedTemplate.tone.instruction,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Recipient email") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedTemplate.buildPrompt(),
            onValueChange = {},
            label = { Text("Generation prompt preview") },
            readOnly = true,
            minLines = 5
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
