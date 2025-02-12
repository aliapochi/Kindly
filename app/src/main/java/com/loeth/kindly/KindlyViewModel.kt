package com.loeth.kindly

import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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


    init {
        loadPromises()
    }

    private fun loadPromises() {
        viewModelScope.launch {
            getAllPromisesUseCase().collect { promises ->
                _promises.value = promises
            }
        }
    }

    fun markAsFulfilled(promiseId: String) {
        viewModelScope.launch {
            try {
                updatePromiseUseCase(promiseId, true)//Update promise to fulfilled into DB

                //Update UI
                _promises.value = _promises.value.map { promise ->
                    if (promise.promiseId == promiseId) promise.copy(isFulfilled = true) else promise
                }

            } catch (e: Exception) {
                Log.e("KindlyViewModel", "Error marking promise as fulfilled: ${e.message}", e)
            }

        }
    }

    fun formatDate(timeStamp: Long): String {
        val stf = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        return stf.format(Date(timeStamp))
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

    fun deletePromise(promiseId: String) {
        viewModelScope.launch {
            try {
                deletePromiseUseCase(promiseId)
                _promises.value = _promises.value.filter { it.promiseId != promiseId}
            }catch (e: Exception) {
                Log.e("KindlyViewModel", "Error deleting promise: ${e.message}", e)
            }
        }
    }

}