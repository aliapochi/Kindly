package com.loeth.kindly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loeth.kindly.KindlyViewModel

@Composable
fun PromiseDetail(
    promiseId: String,
    viewModel: KindlyViewModel = hiltViewModel()
) {
    val promise by viewModel.getPromiseById(promiseId).collectAsState(initial = null)
    if (promise == null) {
        Text(
            text = "Promise not found",
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(onClick = {viewModel.deletePromise(promiseId)})
            {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Promise")
            }
            promise?.let { promise ->
                Text(
                    text = promise.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }?: Text(text = "Promise not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Category:  ${promise.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }?: Text(text = "Promise Category not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Description:  ${promise.description}",
                    style = MaterialTheme.typography.bodyMedium)
            }?: Text(text = "Promise description not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Due Date:  ${viewModel.formatDate(promise.dueDate)}",
                    style = MaterialTheme.typography.bodyMedium)
            }?: Text(text = "Due date unavailable")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Status:  ${if (promise.isFulfilled) "Fulfilled" else "Pending"}",
                    style = MaterialTheme.typography.bodyMedium)
            }
            SpaceBetween()
            Button(
                onClick = {
                    viewModel.markAsFulfilled(promiseId)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !promise!!.isFulfilled
            ){
                Text(text = if (promise!!.isFulfilled) "Fulfilled" else "Mark as Fulfilled")
            }

        }
    }

}