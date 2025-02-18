package com.loeth.kindly.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("""
    SELECT * FROM promise 
    WHERE isFulfilled = 1 
    AND fulfilledDate >= :since 
    ORDER BY fulfilledDate DESC
""")
    fun getRecentActivities(since: Long): Flow<List<PromiseEntity>>

    @Query("SELECT * FROM promise WHERE category = :category")
    fun getPromisesByCategory(category: String): Flow<List<PromiseEntity>>

}