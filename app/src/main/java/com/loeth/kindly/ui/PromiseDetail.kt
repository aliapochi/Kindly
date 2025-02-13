package com.loeth.kindly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
    viewModel: KindlyViewModel = hiltViewModel(),
    onDeleteSuccess: () -> Unit
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
            IconButton(onClick = {
                viewModel.showDeleteConfirmation = true
            })
            {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Promise")
                DeleteConfirmationDialog(viewModel, promiseId)
            }
            promise?.let { promise ->
                Text(
                    text = promise.title,
                    style = MaterialTheme.typography.titleLarge
                )
            } ?: Text(text = "Promise not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Category:  ${promise.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } ?: Text(text = "Promise Category not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Description:  ${promise.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } ?: Text(text = "Promise description not found")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Due Date:  ${viewModel.formatDate(promise.dueDate)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } ?: Text(text = "Due date unavailable")
            SpaceBetween()
            promise?.let { promise ->
                Text(
                    text = "Status:  ${if (promise.isFulfilled) "Fulfilled" else "Pending"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            SpaceBetween()
            Button(
                onClick = {
                    viewModel.markAsFulfilled(promiseId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !promise!!.isFulfilled
            ) {
                Text(text = if (promise!!.isFulfilled) "Fulfilled" else "Mark as Fulfilled")
            }

        }
    }

    DeleteSuccessDialog(viewModel, onDeleteSuccess)
}

@Composable
fun DeleteSuccessDialog(viewModel: KindlyViewModel, onDeleteSuccess: () -> Unit) {
    if (viewModel.showDeleteSuccess) {
        AlertDialog(
            onDismissRequest = {
                viewModel.showDeleteSuccess = false
                onDeleteSuccess()
            },
            title = { Text("Delete Success") },
            text = { Text("Promise deleted successfully") },
            confirmButton = {
                Button(onClick = {
                    viewModel.showDeleteSuccess = false
                    onDeleteSuccess()
                }) {
                    Text("Dismiss")
                }
            }
        )
    }
}


@Composable
fun DeleteConfirmationDialog(
    viewModel: KindlyViewModel,
    promiseId: String,
) {
    if (viewModel.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteConfirmation = false },
            title = { Text("Delete Promise?") },
            text = { Text("Are you sure you want to break your promise? This action cannot be undone!") },
            dismissButton = {
                Button(onClick = { viewModel.showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.deletePromise(promiseId)
                }) {
                    Text("Yes, Delete")
                }
            }
        )
    }

}