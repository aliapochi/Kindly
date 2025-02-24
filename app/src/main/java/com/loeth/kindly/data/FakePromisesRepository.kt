package com.loeth.kindly.data

import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePromisesRepository: PromisesRepository {

    private val promises = mutableListOf<Promise>()

    override fun getAllPromisesStream(): Flow<List<Promise>> {
        return flow { emit(promises) }
    }

    override fun getPromiseById(promiseId: String): Flow<Promise?> {
        return flow { emit(promises.find { it.promiseId == promiseId }) }
    }

    override suspend fun addPromise(promise: Promise) {
        promises.add(promise)
    }

    override suspend fun updatePromise(promiseId: String, isFulfilled: Boolean) {
        promises.find { it.promiseId == promiseId }?.isFulfilled = isFulfilled
    }

    override fun getRecentActivities(since: Long): Flow<List<Promise>> {
        return flow { emit(promises.filter { it.dueDate >= since }) }
    }

    override fun getPromisesByCategory(category: String): Flow<List<Promise>> {
        return flow { emit(promises.filter {it.category == category})}
    }

    override suspend fun deletePromise(promiseId: String) {
        promises.remove(promises.find { it.promiseId == promiseId })
    }
}