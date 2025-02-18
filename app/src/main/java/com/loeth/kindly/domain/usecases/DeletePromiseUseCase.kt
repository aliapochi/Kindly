package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import javax.inject.Inject

class DeletePromiseUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    suspend operator fun invoke(promiseId: String) {
        promisesRepository.deletePromise(promiseId)
    }
}