package com.fresnohernandez99.stpt.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fresnohernandez99.stpt.FileSaverHandler
import com.fresnohernandez99.stpt.FileSaverLauncherHolder
import com.fresnohernandez99.stpt.FolderPickerHandler
import com.fresnohernandez99.stpt.FolderPickerLauncherHolder
import com.fresnohernandez99.stpt.platform.AndroidPlatform
import com.fresnohernandez99.stpt.platform.DATA_STORE_FILE_NAME
import com.fresnohernandez99.stpt.platform.Downloader
import com.fresnohernandez99.stpt.platform.PlatformUtils
import com.fresnohernandez99.stpt.platform.Transcriber
import com.fresnohernandez99.stpt.platform.Platform
import com.fresnohernandez99.stpt.platform.PlatformAudioPlayer
import com.fresnohernandez99.stpt.platform.createDataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<String>(qualifier = named("AppVersion")) {
        val app: Application = get()
        try {
            val packageInfo = app.packageManager.getPackageInfo(app.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    single { FileSaverLauncherHolder() }
    single { FileSaverHandler(get()) }
    single<Platform> { AndroidPlatform(get(named("AppVersion")), get()) }
    single<DataStore<Preferences>> {
        val app: Application = get()
        createDataStore(
            producePath = { app.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
        )
    }
    single { PlatformUtils(get(), get()) }

//    single<SqlDriver> {
//        AndroidSqliteDriver(NoteDatabase.Schema, context = get(), "notes.db")
//    }

    single { PlatformAudioPlayer() }

    single { Downloader(get(), get()) }

    single { Transcriber(get(), get()) }


    // domain
//    single<AudioRecorderInteractor> { AudioRecorderInteractorImpl(get(), get(), get()) }
//    single<SaveAudioNoteInteractor> {
//        SaveAudioNoteInteractorImpl(
//            get(),
//            get(),
//            get(),
//            get(),
//            get()
//        )
//    }

    // export
    single { FolderPickerLauncherHolder() }
    single { FolderPickerHandler(get()) }
//    single<ExportSelectionInteractor> {
//        ExportSelectionInteractorImpl(
//            context = get(),
//            folderPickerHandler = get()
//        )
//    }
}
