package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPromisesUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    //val allPromises: Flow<List<Promise>> = promiseRepository.getAllPromisesStream()
    operator fun invoke(): Flow<List<Promise>> {
        return promisesRepository.getAllPromisesStream()
    }
}