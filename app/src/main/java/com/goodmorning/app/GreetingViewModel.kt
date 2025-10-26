package com.goodmorning.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goodmorning.app.core.domain.GreetingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val greetingUseCase: GreetingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GreetingUiState())
    val uiState: StateFlow<GreetingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            greetingUseCase().collect { message ->
                _uiState.value = GreetingUiState(message)
            }
        }
    }
}

data class GreetingUiState(
    val message: String = ""
)
