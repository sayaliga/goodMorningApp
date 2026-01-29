package com.sayali.wishmate.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class FestivalUiState(
    val isLoading: Boolean = false,
    val events: List<FestivalEvent> = emptyList(),
    val error: String? = null
)

class FestivalEventsViewModel(
    private val repo: GoogleFestivalCalendarRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FestivalUiState())
    val state = _state.asStateFlow()

    fun loadNextDays(days: Int = 7) {
        viewModelScope.launch {
            _state.value = FestivalUiState(isLoading = true)

            runCatching {
                withContext(Dispatchers.IO) {
                    repo.getFestivalsNextDays(days)
                }
            }.onSuccess { list ->
                _state.value = FestivalUiState(events = list)
            }.onFailure { e ->
                _state.value = FestivalUiState(error = e.message)
            }
        }
    }
}

