package com.loeth.kindly.data

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PromiseEntity::class], version = 1, exportSchema = false)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseDao(): PromiseDao

    init {
        Log.d("DatabaseCheck", "PromiseDatabase instance created")
    }
}


