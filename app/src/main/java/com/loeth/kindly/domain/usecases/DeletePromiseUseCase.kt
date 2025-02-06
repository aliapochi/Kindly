package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import javax.inject.Inject

class DeletePromiseUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    suspend operator fun invoke(promise: Promise) {
        promisesRepository.deletePromise(promise)
    }
}