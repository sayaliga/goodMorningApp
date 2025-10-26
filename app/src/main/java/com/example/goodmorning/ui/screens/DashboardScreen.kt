package com.example.goodmorning.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.goodmorning.ui.theme.LocalSpacing

private val sampleCampaigns = listOf(
    "Morning Motivation" to "Scheduled",
    "Weekly Wrap" to "Draft",
    "New Hire Spotlight" to "Live"
)

@Composable
fun DashboardScreen(
    onOpenTemplates: () -> Unit,
    onShare: () -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onOpenTemplates
            ) {
                Text("Browse Templates")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = onShare
            ) {
                Text("Share Campaign")
            }
        }
        Spacer(modifier = Modifier.padding(top = spacing.lg))
        Text(
            text = "Active Campaigns",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.padding(top = spacing.sm))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            items(sampleCampaigns) { (title, status) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(spacing.md)) {
                        Text(text = title, style = MaterialTheme.typography.bodyLarge)
                        Text(text = status, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
