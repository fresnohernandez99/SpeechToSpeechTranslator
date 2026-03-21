package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.platform.TranslatorManagerIos
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun KoinApplication.init(translatorManagerIos: TranslatorManagerIos) {
    modules(
        appModule,
        viewModelModule,
        repositoryModule,
        platformModule(listOf(translatorManagerIos)),
        mapperModule
    )
}

fun initKoinApplication(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config)
        modules(
            appModule,
            viewModelModule,
            repositoryModule,
            platformModule(emptyList()),
            mapperModule
        )
    }
}