package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentActivitiesUseCase @Inject constructor(
    private val promisesRepository: PromisesRepository
) {
    operator fun invoke(since: Long): Flow<List<Promise>> {
        return promisesRepository.getRecentActivities(since)
    }
}
