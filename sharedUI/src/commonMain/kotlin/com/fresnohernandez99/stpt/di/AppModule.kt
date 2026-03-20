package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.data.repository.GreetingRepositoryImpl
import com.fresnohernandez99.stpt.domain.repository.GreetingRepository
import com.fresnohernandez99.stpt.presentation.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<GreetingRepository> { GreetingRepositoryImpl() }
    factoryOf(::HomeViewModel)
}
