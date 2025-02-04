package com.loeth.kindly.domain

data class Promise(
    val promiseId: String,
    val title: String,
    val description: String,
    val category: String,
    val dueDate: Long,
    val isFulfilled: Boolean
)

