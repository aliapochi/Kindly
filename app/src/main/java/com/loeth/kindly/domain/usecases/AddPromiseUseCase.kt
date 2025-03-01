package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import javax.inject.Inject

class AddPromiseUseCase @Inject constructor(private val repository: PromisesRepository) {
    suspend operator fun invoke(promise: Promise) {
        repository.addPromise(promise)
    }
}