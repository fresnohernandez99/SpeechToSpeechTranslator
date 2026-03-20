package com.fresnohernandez99.stpt.domain.repository

import com.fresnohernandez99.stpt.domain.model.Greeting

interface GreetingRepository {
    fun getGreeting(): Greeting
}
