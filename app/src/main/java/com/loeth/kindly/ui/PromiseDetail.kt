package com.loeth.kindly.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.ui.navigation.BottomNavigationBar

@Composable
fun PromiseDetail(
    promiseId: String,
    viewModel: KindlyViewModel = hiltViewModel(),
    onDeleteSuccess: () -> Unit,
    navController: NavHostController
) {
    // Reset delete confirmation when entering screen
    viewModel.showDeleteConfirmation = false

    val promise by viewModel.getPromiseById(promiseId).collectAsState(initial = null)
    val context = LocalContext.current
    Scaffold(
        topBar = { KindlyTopAppBar(navController, "Promise Details") },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
    if (promise == null) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Promise not found",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Row for Delete Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { viewModel.showDeleteConfirmation = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Promise",
                        tint = Color.Red
                    )
                }
            }

            // Delete Confirmation Dialog
            DeleteConfirmationDialog(viewModel, promiseId)

            // Card for Displaying Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = promise!!.title, style = MaterialTheme.typography.titleLarge)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    DetailRow("Category", promise!!.category)
                    DetailRow("Description", promise!!.description)
                    DetailRow("Due Date", viewModel.formatDate(promise!!.dueDate))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: ${if (promise!!.isFulfilled) "Fulfilled" else "Pending"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (promise!!.isFulfilled) Color(0xFF249689) else Color.Red
                        )

                        if (!promise!!.isFulfilled) {
                            Button(
                                onClick = { viewModel.markAsFulfilled(promiseId) },
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text("Mark as Fulfilled")
                            }
                        }
                    }

                }
            }
        }
    }
    }

    // Delete Success Dialog
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
@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}


