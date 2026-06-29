package com.loeth.kindly.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.domain.Promise
import com.loeth.kindly.ui.navigation.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Categories(val item: String, val color: Color) {
    FINANCIAL("Financial", Color(0xFFE3F2FD)),
    RESOURCES("Resources", Color(0xFFF1F8E9)),
    TIME("Time", Color(0xFFFFF3E0)),
    EMOTIONAL_SUPPORT("Emotional Support", Color(0xFFFCE4EC)),
    HEALTH("Health", Color(0xFFE8F5E9)),
    EDUCATION("Education", Color(0xFFEFEBE9)),
    TASK("Task", Color(0xFFF3E5F5)),
    SOCIAL("Social", Color(0xFFE0F2F1)),
    OTHER("Other", Color(0xFFFAFAFA))
}

@Composable
fun AddPromise(viewModel: KindlyViewModel, navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Select Due Date") }
    var selectedCategory by remember { mutableStateOf("Choose Category") }
    val context = LocalContext.current
    val promiseAddedEvent by viewModel.promiseAddedEvent.collectAsState()
    val isLoading by viewModel.inProgress
    var showAlert by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var addedPromise by remember { mutableStateOf<Promise?>(null) }
    
    val scrollState = rememberScrollState()

    val dueDate = remember(selectedDate) {
        if (selectedDate == "Select Due Date") {
            0L
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(selectedDate)?.time ?: 0L
        }
    }

    Scaffold(
        topBar = { KindlyTopAppBar(navController, "Create Promise") },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Section
            Column {
                Text(
                    text = "What's your commitment?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "Making a promise is the first step to keeping it.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Title Field
                    CustomInputField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Promise Title",
                        placeholder = "e.g., Call Mom every Sunday",
                        singleLine = true
                    )

                    // Description Field
                    CustomInputField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        placeholder = "Add more details here...",
                        singleLine = false,
                        maxLines = 4
                    )

                    // Select Date
                    SectionLabel("When is it due?")
                    SelectDueDate(selectedDate) { selectedDate = it }

                    // Select Category
                    SectionLabel("Category")
                    CategoryMenu(selectedCategory) { selectedCategory = it }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Submit Button
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
                                    dueDate = dueDate,
                                    isFulfilled = false
                                )

                                viewModel.addPromise(promise)
                                viewModel.scheduleReminder(context)
                                viewModel.scheduleDueDateReminder(context, dueDate)

                                title = ""
                                description = ""
                                selectedDate = "Select Due Date"
                                selectedCategory = "Choose Category"
                                addedPromise = promise
                                showShareDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            "Create My Promise",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Overlays & Dialogs (Alerts, Share, Loading)
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) { Text("Got it") }
                },
                title = { Text("Almost there!") },
                text = { Text("Please fill in all the details so you can stay committed to your word.") }
            )
        }

        if (showShareDialog && addedPromise != null) {
            AlertDialog(
                onDismissRequest = { showShareDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.shareKindly(context, "I just added a promise on the Kindly app! 📌\n✨ Title: ${addedPromise?.title}\n📅 Due Date: $selectedDate\nYou can do that too: https://play.google.com/store/apps/details?id=com.loeth.kindly")
                            showShareDialog = false
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Share Commitment") }
                },
                dismissButton = {
                    TextButton(onClick = { showShareDialog = false }) { Text("Dismiss") }
                },
                title = { Text("Promise Created! 🌟") },
                text = { Text("Sharing your promise with others increases your chances of keeping it. Want to share?") }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CommonProgressSpinner()
            }
        }
    }

    LaunchedEffect(promiseAddedEvent) {
        if (promiseAddedEvent) {
            Toast.makeText(context, "Promise Saved", Toast.LENGTH_SHORT).show()
            viewModel.resetPromiseAddedEvent()
        }
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    singleLine: Boolean,
    maxLines: Int = 1
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            singleLine = singleLine,
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE1E3E5),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color(0xFFFBFBFC),
                focusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1A1C1E),
        modifier = Modifier.padding(bottom = 0.dp)
    )
}

@Composable
fun CategoryMenu(selectedItem: String, onCategorySelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1A1C1E))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedItem)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Select Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Categories.entries.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(category.color.copy(alpha = 0.5f))
                                    .clickable {
                                        onCategorySelected(category.item)
                                        showDialog = false
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(8.dp).background(category.color, RoundedCornerShape(4.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(text = category.item, fontWeight = FontWeight.Medium)
                            }
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

    OutlinedButton(
        onClick = { openDialog.value = true },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1A1C1E))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedDate)
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
        }
    }

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("Confirm") }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                    onDateSelected(date)
                }
            }
        }
    }
}
