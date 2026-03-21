package com.fresnohernandez99.stpt.di

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fresnohernandez99.stpt.FileSaverHandler
import com.fresnohernandez99.stpt.FileSaverLauncherHolder
import com.fresnohernandez99.stpt.FolderPickerHandler
import com.fresnohernandez99.stpt.FolderPickerLauncherHolder
import com.fresnohernandez99.stpt.platform.AndroidPlatform
import com.fresnohernandez99.stpt.platform.DATA_STORE_FILE_NAME
import com.fresnohernandez99.stpt.platform.Downloader
import com.fresnohernandez99.stpt.platform.Platform
import com.fresnohernandez99.stpt.platform.PlatformUtils
import com.fresnohernandez99.stpt.platform.Transcriber
import com.fresnohernandez99.stpt.platform.createDataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<String>(qualifier = named("AppVersion")) {
        val context: Context = get()
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    single { FileSaverLauncherHolder() }
    single { FileSaverHandler(get()) }
    single<Platform> { AndroidPlatform(get(named("AppVersion")), get()) }
    single<DataStore<Preferences>> {
        val context: Context = get()
        createDataStore(
            producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
        )
    }
    single { PlatformUtils(get(), get()) }

    single { Downloader(get(), get()) }

    single { Transcriber(get(), get() as ComponentActivity) }

    single { FolderPickerLauncherHolder() }
    single { FolderPickerHandler(get()) }
}
