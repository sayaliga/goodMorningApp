package com.sayali.wishmate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource   // 👈 IMPORTANT
import com.sayali.wishmate.R                 // 👈 IMPORTANT
import com.sayali.wishmate.settings.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    current: String,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val labelLanguage = stringResource(R.string.label_language)
    val selectLanguage = stringResource(R.string.label_select_language)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (current.isBlank()) selectLanguage else current,
            onValueChange = {},
            readOnly = true,
            label = { Text(labelLanguage) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AppLanguage.values().forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang.displayName) },
                    onClick = {
                        onLanguageSelected(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}
