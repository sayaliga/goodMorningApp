package com.sayali.wishmate.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    val currentLanguage: StateFlow<AppLanguage?> =
        repo.languageFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch {
            repo.setLanguage(lang)
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LanguageViewModel(SettingsRepository(context)) as T
                }
            }
        }
    }
}
