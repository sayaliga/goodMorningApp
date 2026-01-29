package com.sayali.wishmate

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sayali.wishmate.data.CaptionTemplates
import com.sayali.wishmate.theme.TextBrown

@Composable
fun PromptGrid(
    prompts: List<CaptionTemplates.Prompt>,
    onClick: (CaptionTemplates.Prompt) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        prompts.forEach { prompt ->
            val style = PromptStyles.forPrompt(prompt)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(prompt) },
                colors = CardDefaults.cardColors(
                    containerColor = style.color
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(style.iconRes),
                        contentDescription = prompt.display,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = prompt.display,
                        style = MaterialTheme.typography.titleMedium,
                        color = style.textColor
                    )
                }
            }
        }
    }
}
