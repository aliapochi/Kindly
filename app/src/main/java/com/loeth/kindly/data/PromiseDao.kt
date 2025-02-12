package com.loeth.kindly.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PromiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPromise(promise: PromiseEntity)

    @Query("SELECT * FROM promise")
    fun getAllPromises(): Flow<List<PromiseEntity>>

    @Query("SELECT * FROM promise WHERE promiseId = :promiseId")
    fun getPromiseById(promiseId: String): Flow<PromiseEntity?>

    @Query("UPDATE promise SET isFulfilled = :isFulfilled WHERE promiseId = :promiseId")
    suspend fun updatePromise(promiseId: String, isFulfilled: Boolean)

    @Query("DELETE FROM promise WHERE promiseId = :promiseId")
    suspend fun deletePromise(promiseId: String)


}