package com.loeth.kindly.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.loeth.kindly.BannerAd
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.domain.Promise
import com.loeth.kindly.showInterstitialAd
import com.loeth.kindly.ui.theme.KindlyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Categories(val item: String) {
    FINANCIAL("Financial"),
    RESOURCES("Resources"),
    TIME("Time"),
    EMOTIONAL_SUPPORT("Emotional Support"),
    HEALTH("Health"),
    EDUCATION("Education"),
    TASK("Task"),
    SOCIAL("Social"),
    OTHER("Other")
}

@Composable
fun AddPromise(viewModel: KindlyViewModel, navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Select Due Date") }
    var selectedCategory by remember { mutableStateOf("Choose Category") }
    val context = LocalContext.current
    val promiseAddedEvent by viewModel.promiseAddedEvent.collectAsState()
    var isLoading = viewModel.inProgress.value
    var showAlert by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var addedPromise by remember { mutableStateOf<Promise?>(null) }

    Scaffold(
        topBar = { KindlyTopAppBar(navController, "Add A Promise") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Add a New Promise",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Promise Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Promise Description") },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    SelectDueDate(selectedDate) { selectedDate = it }

                    CategoryMenu(selectedCategory) { selectedCategory = it }

                    Button(
                        onClick = {
                            if (title.isBlank() || description.isBlank() || selectedDate == "Select Due Date" || selectedCategory == "Choose Category") {
                                showAlert = true
                            } else {
                                val promise = Promise(
                                    promiseId = System.currentTimeMillis().toString(),
                                    title = title,
                                    description = description,
                                    category = selectedCategory,
                                    dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        .parse(selectedDate)?.time ?: 0L,
                                    isFulfilled = false
                                )

                                isLoading = true  // Show loading while adding
                                viewModel.addPromise(promise)

                                viewModel.scheduleReminder(context) 

                                // Save promise & show share dialog when added successfully
                                addedPromise = promise
                                showShareDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Add Promise", color = Color.White)
                    }
                    //Spacer(modifier = Modifier.height(16.dp))

                }
            }

            // Show AlertDialog if required fields are empty
            if (showAlert) {
                AlertDialog(
                    onDismissRequest = { showAlert = false },
                    confirmButton = {
                        TextButton(onClick = { showAlert = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Missing Fields") },
                    text = { Text("Please fill in all fields before adding a promise.") }
                )
            }

            // Share AlertDialog after promise is added
            if (showShareDialog && addedPromise != null) {
                AlertDialog(
                    onDismissRequest = { showShareDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.shareKindly(
                                context,
                                """
    I just added a promise on the Kindly app! 📌
    
    ✨ Title: ${addedPromise?.title}
    📅 Due Date: $selectedDate
    📂 Category: $selectedCategory
    
    You can do that too: https://play.google.com/store/apps/details?id=com.loeth.kindly&pcampaignid=web_share
""".trimIndent()
                            )
                            showShareDialog = false
                        }) {
                            Text("Share")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showShareDialog = false
                            showInterstitialAd(context){}
                        }) {
                            Text("Dismiss")
                        }
                    },
                    title = { Text("Promise Added!") },
                    text = { Text("You have successfully added a new promise. Would you like to share it?") }
                )
            }

            // Show loading overlay while adding promise
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CommonProgressSpinner()
                }
            }
        }
    }

    // Show Toast & Reset Loading State after promise is added
    LaunchedEffect(promiseAddedEvent) {
        if (promiseAddedEvent) {
            Toast.makeText(context, "Promise Added", Toast.LENGTH_SHORT).show()
            viewModel.resetPromiseAddedEvent()

            isLoading = false // Hide loading
        }
    }
}

@Composable
fun CategoryMenu(selectedItem: String, onCategorySelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedItem)
            Spacer(Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x00000000)) // Semi-transparent background
                        .clickable { showDialog = false } // Dismiss on outside click
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Select Category",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Categories.entries.forEach { category ->
                            Text(
                                text = category.item,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clickable {
                                        onCategorySelected(category.item)
                                        showDialog = false
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDueDate(selectedDate: String, onDateSelected: (String) -> Unit) {
    val openDialog = remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { openDialog.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            Spacer(Modifier.width(8.dp))
            Text(text = selectedDate)
        }

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
                        val today = System.currentTimeMillis()
                        if (millis >= today) {
                            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(Date(millis))
                            onDateSelected(date)
                        }
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
        //AddPromise(viewModel = FakeViewModel()) // Use a fake ViewModel for preview
    }
}
