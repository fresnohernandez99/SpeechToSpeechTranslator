package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.audio.presentation.AudioImportViewModel
import com.fresnohernandez99.stpt.audio.presentation.AudioPlayerViewModel
import com.fresnohernandez99.stpt.audio.presentation.AudioRecorderViewModel
import com.fresnohernandez99.stpt.audio.presentation.mappers.AudioPlayerPresentationToUiMapper
import com.fresnohernandez99.stpt.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.fresnohernandez99.stpt.data.repository.GreetingRepositoryImpl
import com.fresnohernandez99.stpt.data.repository.PreferencesRepository
import com.fresnohernandez99.stpt.domain.repository.GreetingRepository
import com.fresnohernandez99.stpt.platform.presentation.PlatformViewModel
import com.fresnohernandez99.stpt.presentation.home.HomeViewModel
import com.fresnohernandez99.stpt.presentation.modelDownloader.ModelDownloaderViewModel
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
    viewModelOf(::ModelDownloaderViewModel)
    viewModelOf(::AudioRecorderViewModel)
    viewModelOf(::AudioPlayerViewModel)
    viewModelOf(::AudioImportViewModel)
}

val mapperModule = module {
    single { AudioPlayerPresentationToUiMapper() }
    single { AudioRecorderPresentationToUiMapper() }
}
