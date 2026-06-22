package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.platform.LanguageIdManagerIos
import com.fresnohernandez99.stpt.platform.TranslatorManagerIos
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

// Ios Init
fun KoinApplication.init(
    translatorManagerIos: TranslatorManagerIos,
    languageIdManagerIos: LanguageIdManagerIos,
) {
    modules(
        appModule,
        viewModelModule,
        repositoryModule,
        platformModule(listOf(translatorManagerIos, languageIdManagerIos)),
        mapperModule
    )
}

// Android init
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