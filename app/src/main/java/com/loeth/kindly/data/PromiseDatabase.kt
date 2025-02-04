package com.loeth.kindly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PromiseEntity::class], version = 1, exportSchema = false)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseDao(): PromiseDao

    companion object {
        @Volatile
        private var INSTANCE: PromiseDatabase? = null

        fun getDatabase(context: Context): PromiseDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PromiseDatabase::class.java,
                    "promise_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
