package com.goodmorning.app.core.domain

import kotlinx.coroutines.flow.Flow

interface GreetingRepository {
    fun greet(): Flow<String>
}
