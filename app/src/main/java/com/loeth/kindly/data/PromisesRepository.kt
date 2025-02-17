package com.loeth.kindly.data

import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow

interface PromisesRepository {
    fun getAllPromisesStream(): Flow<List<Promise>>

    fun getPromiseById(promiseId: String): Flow<Promise?>

    suspend fun addPromise(promise: Promise)

    suspend fun updatePromise(promiseId: String, isFulfilled: Boolean)

    fun getRecentActivities(since: Long): Flow<List<Promise>>

    fun getPromisesByCategory(category: String): Flow<List<Promise>>

    suspend fun deletePromise(promiseId: String)

}