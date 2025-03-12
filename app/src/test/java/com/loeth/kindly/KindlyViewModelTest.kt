package com.loeth.kindly

import com.loeth.kindly.data.FakePromisesRepository
import com.loeth.kindly.domain.Promise
import com.loeth.kindly.domain.usecases.AddPromiseUseCase
import com.loeth.kindly.domain.usecases.DeletePromiseUseCase
import com.loeth.kindly.domain.usecases.GetAllPromisesUseCase
import com.loeth.kindly.domain.usecases.GetPromiseByIdUseCase
import com.loeth.kindly.domain.usecases.GetPromisesByCategoryUseCase
import com.loeth.kindly.domain.usecases.GetRecentActivitiesUseCase
import com.loeth.kindly.domain.usecases.UpdatePromiseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class KindlyViewModelTest {

    private lateinit var viewModel: KindlyViewModel
    private lateinit var repository: FakePromisesRepository
    @Mock
    private lateinit var getPromiseByIdUseCase: GetPromiseByIdUseCase
    @Mock
    private lateinit var getAllPromisesUseCase: GetAllPromisesUseCase
    @Mock
    private lateinit var addPromiseUseCase: AddPromiseUseCase
    @Mock
    private lateinit var updatePromiseUseCase: UpdatePromiseUseCase
    @Mock
    private lateinit var deletePromiseUseCase: DeletePromiseUseCase
    @Mock
    private lateinit var getRecentActivitiesUseCase: GetRecentActivitiesUseCase
    @Mock
    private lateinit var getPromisesByCategoryUseCase: GetPromisesByCategoryUseCase

    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = FakePromisesRepository()
        viewModel = KindlyViewModel(
            getAllPromisesUseCase,
            getPromiseByIdUseCase,
            addPromiseUseCase,
            updatePromiseUseCase,
            deletePromiseUseCase,
            getRecentActivitiesUseCase,
            getPromisesByCategoryUseCase
        )

    }
    private val promise = Promise(
        "123",
        "mom",
        "mommy's car",
        "financial",
        1234567886,
        true)

    @Test
    fun getInProgress() {
    }

    @Test
    fun getPromises() {
    }

    @Test
    fun getPromiseAddedEvent() {
    }

    @Test
    fun getShowDeleteSuccess() {
    }

    @Test
    fun setShowDeleteSuccess() {
    }

    @Test
    fun getShowDeleteConfirmation() {
    }

    @Test
    fun setShowDeleteConfirmation() {
    }

    @Test
    fun getRecentActivities() {
    }

    @Test
    fun getFulfilledPromisesCount() {
    }

    @Test
    fun getPromisesByCategory() {
    }

    @Test
    fun scheduleReminder() {
    }

    @Test
    fun shareKindly() {
    }

    @Test
    fun fetchRecentActivities() {
    }

    @Test
    fun markAsFulfilled() {
    }

    @Test
    fun formatDate() {
    }

    @Test
    fun getPromiseById() {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `add promise should set inProgress to true and false when done`() = runTest{
        `when`(addPromiseUseCase(promise)).thenReturn(Unit)

        viewModel.addPromise(promise)
        assert(viewModel.inProgress.value)
        advanceUntilIdle()
        assert(!viewModel.inProgress.value)
    }

    @Test
    fun resetPromiseAddedEvent() {
    }

    @Test
    fun deletePromise() {
    }
}