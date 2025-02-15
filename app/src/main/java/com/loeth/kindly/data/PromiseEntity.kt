package com.loeth.kindly.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loeth.kindly.domain.Promise

@Entity(tableName = "promise")
data class PromiseEntity(
    @PrimaryKey
    val promiseId: String,
    val title: String,
    val description: String,
    val category: String,
    val dueDate: Long,
    val isFulfilled: Boolean,
    val fulfilledDate: Long? = null
)

fun PromiseEntity.toDomainModel(): Promise {
    return Promise(
        promiseId = this.promiseId,
        title = this.title,
        description = this.description,
        category = this.category,
        dueDate = this.dueDate,
        isFulfilled = this.isFulfilled,
        fulfilledDate = this.fulfilledDate!!
    )
}
