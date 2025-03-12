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
import com.loeth.kindly.domain.usecases.GetRecentActivitiesUseCase
import com.loeth.kindly.domain.usecases.UpdatePromiseUseCase
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.loeth.kindly.domain.usecases.GetPromisesByCategoryUseCase
import com.loeth.kindly.ui.Categories
import java.util.concurrent.TimeUnit

@HiltViewModel
class KindlyViewModel @Inject constructor(
    private val getAllPromisesUseCase: GetAllPromisesUseCase,
    private val getPromiseByIdUseCase: GetPromiseByIdUseCase,
    private val addPromisesUseCase: AddPromiseUseCase,
    private val updatePromiseUseCase: UpdatePromiseUseCase,
    private val deletePromiseUseCase: DeletePromiseUseCase,
    private val getRecentActivitiesUseCase: GetRecentActivitiesUseCase,
    private val getPromisesByCategoryUseCase: GetPromisesByCategoryUseCase
) : ViewModel() {

    val inProgress = mutableStateOf(false)

    private val _promises = MutableStateFlow<List<Promise>>(emptyList())
    val promises: StateFlow<List<Promise>> = _promises

    private val _promiseAddedEvent = MutableStateFlow(false)
    val promiseAddedEvent: StateFlow<Boolean> = _promiseAddedEvent

    var showDeleteSuccess by mutableStateOf(false)
    var showDeleteConfirmation by mutableStateOf(false)

    private val _recentActivities = MutableStateFlow<List<Promise>>(emptyList())
    val recentActivities: StateFlow<List<Promise>> = _recentActivities

    init {
        loadPromises()
    }

    val fulfilledPromisesCount: StateFlow<Int> = promises
        .map { it.count { promise -> promise.isFulfilled } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val categories = Categories.entries.map { it.item }


    // Get a dynamic map of category counts
    val promisesByCategory = categories.associateWith { category ->
        getPromisesByCategoryUseCase(category)
            .map { promises -> promises.count { it.isFulfilled } }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)
    }

    private fun loadPromises() {
        viewModelScope.launch {
            getAllPromisesUseCase().collect { promises ->
                _promises.value = promises
            }
        }
    }
    //

    fun scheduleReminder(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }


    fun shareKindly(context: Context, message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun fetchRecentActivities() {
        viewModelScope.launch {
            getRecentActivitiesUseCase(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)
                .collect { activities ->
                    _recentActivities.value = activities
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


}