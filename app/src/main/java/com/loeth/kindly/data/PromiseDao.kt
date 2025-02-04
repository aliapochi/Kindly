package com.loeth.kindly.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface PromiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromise(promise: PromiseEntity)

    @Query("SELECT * FROM promise")
    fun getAllPromises(): Flow<List<PromiseEntity?>>

    @Query("SELECT * FROM promise WHERE promiseId = :promiseId")
    fun getPromiseById(promiseId: String): Flow<PromiseEntity?>

    @Update
    suspend fun updatePromise(promise: PromiseEntity)

    @Delete
    suspend fun deletePromise(promise: PromiseEntity)


}