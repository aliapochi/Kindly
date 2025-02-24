package com.loeth.kindly.domain.usecases

import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.Promise
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class AddPromiseUseCaseTest {

    private lateinit var addPromiseUseCase: AddPromiseUseCase

    @Mock
    private lateinit var repository: PromisesRepository

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        addPromiseUseCase = AddPromiseUseCase(repository)
    }

    @Test
    fun `test adding a promise`() = runTest {
        val promise = Promise(
            promiseId = "1",
            title = "New promise",
            description = "",
            category = "",
            dueDate = 0L,
            isFulfilled = false
        )
        addPromiseUseCase(promise)
        verify(repository).addPromise(promise)
    }

}