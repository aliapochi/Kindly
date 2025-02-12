package com.loeth.kindly.data

import com.loeth.kindly.domain.Promise
import com.loeth.kindly.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class OfflinePromisesRepository(private val promiseDao: PromiseDao) : PromisesRepository {

    override fun getAllPromisesStream(): Flow<List<Promise>> {
        return promiseDao.getAllPromises()
            .map { entities -> entities.map { it.toDomainModel() } } // Convert to domain model
    }

    override fun getPromiseById(promiseId: String): Flow<Promise?> {
        return promiseDao.getPromiseById(promiseId)
            .map { it?.toDomainModel() } // Convert to domain model
    }

    override suspend fun addPromise(promise: Promise) {
        promiseDao.addPromise(promise.toEntity()) // Convert to entity before saving
    }

    override suspend fun updatePromise(promiseId: String, isFulfilled: Boolean) {
        promiseDao.updatePromise(promiseId, isFulfilled) // Convert to entity before updating
    }

    override suspend fun deletePromise(promiseId: String) {
        promiseDao.deletePromise(promiseId) // Convert to entity before deleting
    }
}
