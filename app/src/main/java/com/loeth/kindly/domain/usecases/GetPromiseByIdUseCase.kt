package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPromiseByIdUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    operator fun invoke(promiseId: String): Flow<Promise?> {
        return promisesRepository.getPromiseById(promiseId)
    }
}