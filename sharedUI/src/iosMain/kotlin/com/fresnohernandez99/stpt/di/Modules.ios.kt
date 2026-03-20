package com.fresnohernandez99.stpt.di

import com.fresnohernandez99.stpt.audio.domain.AudioRecorderInteractor
import com.fresnohernandez99.stpt.platform.DATA_STORE_FILE_NAME
import com.fresnohernandez99.stpt.platform.Downloader
import com.fresnohernandez99.stpt.platform.IOSPlatform
import com.fresnohernandez99.stpt.platform.PlatformAudioPlayer
import com.fresnohernandez99.stpt.platform.PlatformUtils
import com.fresnohernandez99.stpt.platform.Transcriber
import com.fresnohernandez99.stpt.platform.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule = module {

    single<com.fresnohernandez99.stpt.platform.Platform> { IOSPlatform() }
    single { PlatformUtils() }
    single {
        createDataStore {
            val directory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
        }
    }

//    single<SqlDriver> {
//        NativeSqliteDriver(NoteDatabase.Schema, "notes.db")
//    }

    single<String>(qualifier = named("AppVersion")) {
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
            ?: "Unknown"
    }


    single { PlatformAudioPlayer() }

    single { Downloader() }

    single { Transcriber() }

    // domain
//    single<AudioRecorderInteractor> { AudioRecorderInteractorImpl(get(), get()) }
//    single<ExportSelectionInteractor> { ExportSelectionInteractorImpl() }
}