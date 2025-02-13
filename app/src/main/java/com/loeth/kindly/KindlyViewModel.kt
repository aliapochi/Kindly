package com.loeth.kindly

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import java.util.Calendar
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

    private val _promiseAddedEvent = MutableStateFlow(false)
    val promiseAddedEvent: StateFlow<Boolean> = _promiseAddedEvent

    var showDeleteSuccess by mutableStateOf(false)
    var showDeleteConfirmation by mutableStateOf(false)


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
            try{
                addPromisesUseCase(promise)
                _promiseAddedEvent.value = true
            }catch (e: Exception){
                Log.e("KindlyViewModel", "Error adding promise: ${e.message}", e)
            }
            inProgress.value = false

        }
    }
    fun resetPromiseAddedEvent() {
        _promiseAddedEvent.value = false
    }


    fun deletePromise(promiseId: String) {
        viewModelScope.launch {
            try {
                deletePromiseUseCase(promiseId)

                _promises.value = _promises.value.filter { it.promiseId != promiseId}//Update UI

                showDeleteSuccess = true // Show the delete success message

            }catch (e: Exception) {
                Log.e("KindlyViewModel", "Error deleting promise: ${e.message}", e)
            }
        }
    }

    fun getStartOfWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek) // Set to Monday (or Sunday based on locale)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getEndOfWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, 6) // End of the week
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return calendar.timeInMillis
    }

    fun getDaysUntilDue(dueDate: Long): Int {
        val currentDate = Calendar.getInstance().timeInMillis
        val diff = dueDate - currentDate
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }


}