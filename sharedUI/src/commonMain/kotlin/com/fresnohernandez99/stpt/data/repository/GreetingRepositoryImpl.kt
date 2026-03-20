package com.fresnohernandez99.stpt.data.repository

import com.fresnohernandez99.stpt.domain.model.Greeting
import com.fresnohernandez99.stpt.domain.repository.GreetingRepository

class GreetingRepositoryImpl : GreetingRepository {
    override fun getGreeting(): Greeting {
        return Greeting(message = "Hello from Domain Layer!")
    }
}
