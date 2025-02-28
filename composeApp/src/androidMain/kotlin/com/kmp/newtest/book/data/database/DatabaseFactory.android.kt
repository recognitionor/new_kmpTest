package com.kmp.newtest.book.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val ctx: Context) {
    actual fun create(): RoomDatabase.Builder<FavoriteBookDatabase> {
        val appCtx = ctx.applicationContext
        val dbFile = appCtx.getDatabasePath(FavoriteBookDatabase.DB_NAME)
        return Room.databaseBuilder(context = appCtx, name = dbFile.absolutePath)
    }
}