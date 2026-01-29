package com.sayali.wishmate

import androidx.compose.ui.graphics.Color
import com.sayali.wishmate.data.CaptionTemplates
import com.sayali.wishmate.theme.MorningYellow
import com.sayali.wishmate.theme.NightBlue
import com.sayali.wishmate.theme.BirthdayPink
import com.sayali.wishmate.theme.AnniversaryPurple
import com.sayali.wishmate.theme.DiwaliOrange
import com.sayali.wishmate.theme.TextBrown

data class PromptStyle(
    val color: Color,
    val iconRes: Int,
    val textColor: Color
)


object PromptStyles {

    fun forPrompt(prompt: CaptionTemplates.Prompt): PromptStyle {
        return when (prompt.category.trim()) {

            "Morning" -> PromptStyle(
                color = MorningYellow,
                iconRes = R.drawable.morning,
                textColor = TextBrown  // readable on yellow
            )

            "Night" -> PromptStyle(
                color = NightBlue,
                iconRes = R.drawable.night,
                textColor = Color.White
            )

            "Birthday" -> PromptStyle(
                color = BirthdayPink,
                iconRes = R.drawable.birthday,
                textColor = Color.White
            )

            "Anniversary" -> PromptStyle(
                color = AnniversaryPurple,
                iconRes = R.drawable.anniversary,
                textColor = Color.White
            )

            "Festival" -> PromptStyle(
                color = DiwaliOrange,
                iconRes = R.drawable.diwali,
                textColor = TextBrown
            )

            else -> PromptStyle(
                color = MorningYellow.copy(alpha = 0.4f),
                iconRes = R.drawable.logo,
                textColor = TextBrown
            )
        }
    }
}

