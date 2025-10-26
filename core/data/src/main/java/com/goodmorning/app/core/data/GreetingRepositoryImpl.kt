package com.goodmorning.app.core.data

import com.goodmorning.app.core.domain.GreetingRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class GreetingRepositoryImpl @Inject constructor(
    private val greetingDataSource: GreetingLocalDataSource
) : GreetingRepository {

    override fun greet(): Flow<String> = greetingDataSource.greeting
}

class GreetingLocalDataSource @Inject constructor() {
    private val _greeting = MutableStateFlow("Good morning!")
    val greeting: Flow<String> = _greeting.asStateFlow()
}
