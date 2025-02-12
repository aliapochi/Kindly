package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import javax.inject.Inject

class UpdatePromiseUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    suspend operator fun invoke(promiseId: String, isFulfilled: Boolean) {
        promisesRepository.updatePromise(promiseId, isFulfilled)
    }
}