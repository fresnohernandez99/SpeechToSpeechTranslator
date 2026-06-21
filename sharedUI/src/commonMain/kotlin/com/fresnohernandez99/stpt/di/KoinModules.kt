package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.InitViewModel
import com.fresnohernandez99.stpt.data.repository.DictRepositoryImpl
import com.fresnohernandez99.stpt.data.repository.PreferencesRepositoryImpl
import com.fresnohernandez99.stpt.domain.repository.DictRepository
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import com.fresnohernandez99.stpt.modelDownloader.ModelDownloaderViewModel
import com.fresnohernandez99.stpt.modelDownloader.ModelSelection
import com.fresnohernandez99.stpt.platform.presentation.PlatformViewModel
import com.fresnohernandez99.stpt.presentation.dictsManage.DictsManageViewModel
import com.fresnohernandez99.stpt.presentation.home.HomeViewModel
import com.fresnohernandez99.stpt.presentation.modelSelection.ModelSelectionViewModel
import com.fresnohernandez99.stpt.presentation.settings.SettingsViewModel
import com.fresnohernandez99.stpt.transcription.TranscriptionViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal expect fun platformModule(args: List<Any> = emptyList()): Module

val appModule = module {
    factory { ModelSelection(get()) }
}

val repositoryModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<DictRepository> { DictRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::PlatformViewModel)
    viewModelOf(::ModelDownloaderViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ModelSelectionViewModel)
    viewModelOf(::TranscriptionViewModel)
    viewModelOf(::DictsManageViewModel)
    viewModelOf(::InitViewModel)
}

val mapperModule = module {
}
