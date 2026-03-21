package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.data.repository.GreetingRepositoryImpl
import com.fresnohernandez99.stpt.data.repository.PreferencesRepository
import com.fresnohernandez99.stpt.domain.repository.GreetingRepository
import com.fresnohernandez99.stpt.platform.presentation.PlatformViewModel
import com.fresnohernandez99.stpt.presentation.home.HomeViewModel
import com.fresnohernandez99.stpt.modelDownloader.ModelDownloaderViewModel
import com.fresnohernandez99.stpt.modelDownloader.ModelSelection
import com.fresnohernandez99.stpt.presentation.modelSelection.ModelSelectionViewModel
import com.fresnohernandez99.stpt.presentation.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal expect val platformModule: Module

val appModule = module {
    factory { ModelSelection(get()) }
}

val repositoryModule = module {
    single<GreetingRepository> { GreetingRepositoryImpl() }
    single<PreferencesRepository> { PreferencesRepository(get()) }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::PlatformViewModel)
    viewModelOf(::ModelDownloaderViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ModelSelectionViewModel)
}

val mapperModule = module {
}
