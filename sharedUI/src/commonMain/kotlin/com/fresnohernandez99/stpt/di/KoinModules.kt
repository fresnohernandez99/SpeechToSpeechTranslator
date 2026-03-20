package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.data.repository.GreetingRepositoryImpl
import com.fresnohernandez99.stpt.data.repository.PreferencesRepository
import com.fresnohernandez99.stpt.domain.repository.GreetingRepository
import com.fresnohernandez99.stpt.platform.presentation.PlatformViewModel
import com.fresnohernandez99.stpt.presentation.home.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal expect val platformModule: Module

val appModule = module {
}

val repositoryModule = module {
    single<GreetingRepository> { GreetingRepositoryImpl() }
    singleOf(::PreferencesRepository)
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::PlatformViewModel)
}