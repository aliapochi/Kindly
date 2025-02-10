package com.loeth.kindly

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loeth.kindly.domain.Promise
import com.loeth.kindly.domain.usecases.AddPromiseUseCase
import com.loeth.kindly.domain.usecases.DeletePromiseUseCase
import com.loeth.kindly.domain.usecases.GetAllPromisesUseCase
import com.loeth.kindly.domain.usecases.GetPromiseByIdUseCase
import com.loeth.kindly.domain.usecases.UpdatePromiseUseCase
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class KindlyViewModel @Inject constructor(
    private val getAllPromisesUseCase: GetAllPromisesUseCase,
    private val getPromiseByIdUseCase: GetPromiseByIdUseCase,
    private val addPromisesUseCase: AddPromiseUseCase,
    private val updatePromiseUseCase: UpdatePromiseUseCase,
    private val deletePromiseUseCase: DeletePromiseUseCase
) : ViewModel() {

    val inProgress = mutableStateOf(false)

    private val _promises = MutableStateFlow<List<Promise>>(emptyList())
            val promises: StateFlow<List<Promise>> = _promises


    init{
        loadPromises()
    }

    private fun loadPromises() {
        viewModelScope.launch {
            getAllPromisesUseCase().collect { promises ->
                _promises.value = promises
            }
        }
    }

    fun getPromiseById(promiseId: String) = getPromiseByIdUseCase(promiseId)

    fun addPromise(promise: Promise) {
        inProgress.value = true
        viewModelScope.launch {
            addPromisesUseCase(promise)

            inProgress.value = false

            promise.title = ""
            promise.description = ""
            promise.category = ""
            promise.dueDate = 0

        }

    }

    fun updatePromise(promise: Promise) {
        viewModelScope.launch {
            updatePromiseUseCase(promise)
        }
    }

    fun deletePromise(promise: Promise) {
        viewModelScope.launch {
            deletePromiseUseCase(promise)
        }
    }

}