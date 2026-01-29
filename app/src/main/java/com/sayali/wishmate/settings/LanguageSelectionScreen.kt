package com.sayali.wishmate.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sayali.wishmate.settings.AppLanguage

@Composable
fun LanguageSelectionScreen(
    selected: AppLanguage?,
    onSelect: (AppLanguage) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose your language",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(24.dp))

        AppLanguage.values().forEach { lang ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelect(lang) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lang.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (lang == selected) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Selected",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
