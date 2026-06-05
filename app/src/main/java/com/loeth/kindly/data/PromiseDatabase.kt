package com.loeth.kindly.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PromiseEntity::class], version = 1, exportSchema = false)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseDao(): PromiseDao
}


