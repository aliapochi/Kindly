package com.loeth.kindly.data

import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow

interface PromisesRepository {
    fun getAllPromisesStream(): Flow<List<Promise>>

    fun getPromiseById(promiseId: String): Flow<Promise?>

    suspend fun addPromise(promise: Promise)

    suspend fun updatePromise(promise: Promise)

    suspend fun deletePromise(promise: Promise)

}