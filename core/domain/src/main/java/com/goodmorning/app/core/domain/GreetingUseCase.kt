package com.goodmorning.app.core.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GreetingUseCase @Inject constructor(
    private val repository: GreetingRepository
) {
    operator fun invoke(): Flow<String> = repository.greet()
}
