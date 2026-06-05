package com.loeth.kindly.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.loeth.kindly.BannerAd
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.R
import com.loeth.kindly.ui.navigation.BottomNavigationBar
import com.loeth.kindly.ui.navigation.Screen
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KindlyTopAppBar(
    navController: NavHostController,
    currentScreenTitle: String
) {
    TopAppBar(
        title = {
            Text(
                text = currentScreenTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 0.5.sp
                )
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White, // Clean white header
            titleContentColor = Color(0xFF1A1C1E),
            actionIconContentColor = Color(0xFF1A1C1E)
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun Dashboard(navController: NavHostController) {
    val context = LocalContext.current
    ExitDialog(context)
    val scrollState = rememberScrollState()
    val viewModel: KindlyViewModel = hiltViewModel()

    Scaffold(
        topBar = { KindlyTopAppBar(navController, "Dashboard") },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Welcome, Section
            Column {
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "You're doing great on your commitments.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            ActivePromisesCard(viewModel)

            PromisesDueSoonCard(navController, viewModel)

            RecentActivityCard(viewModel)

            ImpactSummaryCard(viewModel)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ActivePromisesCard(viewModel: KindlyViewModel) {
    val promises by viewModel.promises.collectAsState()
    val activeCount = promises.count { !it.isFulfilled }
    val totalCount = promises.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Column {
                        Text(
                            text = "Active",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = activeCount.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = totalCount.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Keep up the momentum!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.handshake),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}

@Composable
fun PromisesDueSoonCard(navController: NavHostController, viewModel: KindlyViewModel) {
    val totalPromises = viewModel.promises.collectAsState()
    val today = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }

    val promisesDueSoon = totalPromises.value.filter { promise ->
        val dueDate = Calendar.getInstance().apply { timeInMillis = promise.dueDate }
        dueDate.after(today) || dueDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }.take(3)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due Soon",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { navController.navigate(Screen.AllPromises.route) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (promisesDueSoon.isEmpty()) {
                Text(
                    text = "No upcoming deadlines. Take a breath!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                promisesDueSoon.forEachIndexed { index, promise ->
                    val dueDate = Calendar.getInstance().apply { timeInMillis = promise.dueDate }
                    val daysLeft = (dueDate.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (daysLeft <= 1) Color(0xFFFFF1F2) else Color(0xFFF1F5F9), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.stopclock),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (daysLeft <= 1) Color(0xFFFF5963) else Color(0xFF64748B)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = promise.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1C1E)
                            )
                            Text(
                                text = when {
                                    daysLeft <= 0L -> "Due today"
                                    daysLeft == 1L -> "Due in 1 day"
                                    else -> "Due in $daysLeft days"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (daysLeft <= 1) Color(0xFFFF5963) else Color.Gray
                            )
                        }
                    }
                    if (index < promisesDueSoon.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp, color = Color(0xFFF1F3F4))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivityCard(viewModel: KindlyViewModel) {
    val recentActivities by viewModel.recentActivities.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecentActivities()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (recentActivities.isEmpty()) {
                Text(
                    text = "Start by completing a promise!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                recentActivities.take(3).forEachIndexed { index, promise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFECFDF5), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF10B981)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = promise.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1C1E)
                            )
                            Text(
                                text = "Completed ${viewModel.formatDate(promise.fulfilledDate)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                    if (index < recentActivities.size - 1 && index < 2) {
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp, color = Color(0xFFF1F3F4))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImpactSummaryCard(viewModel: KindlyViewModel) {
    val categoryCounts = viewModel.promisesByCategory.mapValues { it.value.collectAsState().value }
        .filterValues { it > 0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Impact Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 3
            ) {
                categoryCounts.forEach { (category, count) ->
                    val color = Categories.entries.find { it.item == category }?.color ?: Color(0xFFF1F3F4)
                    Column(
                        modifier = Modifier
                            .background(color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$count", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = category, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${viewModel.fulfilledPromisesCount.collectAsState().value}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Promise(s) Fulfilled",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ExitDialog(context: Context) {
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Kindly?") },
            text = { Text("Are you sure you want to exit? We'll miss you!") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    (context as? Activity)?.finish()
                }) {
                    Text("Exit", color = Color(0xFFFF5963), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Stay", fontWeight = FontWeight.SemiBold)
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}
