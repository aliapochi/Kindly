package com.loeth.kindly.domain

import com.loeth.kindly.data.PromiseEntity

data class Promise(
    val promiseId: String,
    val title: String,
    val description: String,
    val category: String,
    val dueDate: Long,
    val isFulfilled: Boolean
)

fun Promise.toEntity(): PromiseEntity {
    return PromiseEntity(
        promiseId = this.promiseId,
        title = this.title,
        description = this.description,
        category = this.category,
        dueDate = this.dueDate,
        isFulfilled = this.isFulfilled
    )
}

