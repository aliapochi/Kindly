package com.loeth.kindly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promise")
data class PromiseEntity(
    @PrimaryKey
    val promiseId: String,
    val title: String,
    val description: String,
    val category: String,
    val dueDate: Long,
    val isFulfilled: Boolean
)
