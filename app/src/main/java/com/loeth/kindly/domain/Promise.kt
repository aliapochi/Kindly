package com.loeth.kindly.domain

import com.loeth.kindly.data.PromiseEntity

data class Promise(
    val promiseId: String,
    var title: String,
    var description: String,
    var category: String,
    var dueDate: Long,
    var isFulfilled: Boolean,
    var fulfilledDate: Long = 0L
)

fun Promise.toEntity(): PromiseEntity {
    return PromiseEntity(
        promiseId = this.promiseId,
        title = this.title,
        description = this.description,
        category = this.category,
        dueDate = this.dueDate,
        isFulfilled = this.isFulfilled,
        fulfilledDate = this.fulfilledDate
    )
}

