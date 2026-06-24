package com.fresnohernandez99.stpt.room

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>
