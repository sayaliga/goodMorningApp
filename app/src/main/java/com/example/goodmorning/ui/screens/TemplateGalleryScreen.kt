package com.example.goodmorning.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.goodmorning.ui.theme.LocalSpacing

private val sampleTemplates = listOf(
    "Sunrise Salutation",
    "Gratitude Generator",
    "Coffee Chat Kickoff",
    "Monday Momentum",
    "Friday Feel-Good",
    "Birthday Spotlight"
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        items(sampleTemplates) { template ->
            Card(
                onClick = { onTemplateSelected(template) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = template,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(spacing.md)
                )
            }
        }
    }
}
