package com.fresnohernandez99.stpt.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(ctx: Any?): RoomDatabase.Builder<AppDatabase> {
    require(ctx is Context) { "Android Context is required" }
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("translated_items.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
