package com.loeth.kindly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.domain.Promise
import com.loeth.kindly.ui.theme.KindlyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Categories(val item: String) {
    FINANCIAL("Financial"),
    RESOURCES("Resources"),
    TIME("Time"),
    EMOTIONAL_SUPPORT("EmotionalSupport"),
    HEALTH("Health"),
    EDUCATION("Education"),
    TASK("Task"),
    SOCIAL("Social"),
    OTHER("Other")
}

@Composable
fun AddPromise(viewModel: KindlyViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Choose Category") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Color.White)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Promise Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Promise Description") },
            modifier = Modifier.fillMaxWidth()
        )
        SelectDueDate(selectedDate) { selectedDate = it }
        CategoryMenu(selectedCategory) { selectedCategory = it }

        Button(
            onClick = {
                val promise = Promise(
                    promiseId = System.currentTimeMillis().toString(), // Unique ID
                    title = title,
                    description = description,
                    category = selectedCategory,
                    dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(selectedDate)?.time ?: 0L,
                    isFulfilled = false
                )
                viewModel.addPromise(promise)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Promise")
        }
    }
}

@Composable
fun CategoryMenu(selectedItem: String, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = true }) {
            Text(text = selectedItem)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Categories.entries.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category.item) },
                    onClick = {
                        onCategorySelected(category.item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDueDate(selectedDate: String, onDateSelected: (String) -> Unit) {
    val openDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Select Due Date") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.clickable { openDialog.value = true }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (openDialog.value) {
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Confirm")
                    }
                }
            ) {
                val datePickerState = rememberDatePickerState()
                DatePicker(state = datePickerState)

                LaunchedEffect(datePickerState.selectedDateMillis) {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Date(millis))
                        onDateSelected(date)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun AddPromisePreview() {
    KindlyTheme {
    }
}