package com.loeth.kindly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.R
import com.loeth.kindly.ui.navigation.Screen
import com.loeth.kindly.ui.theme.KindlyTheme
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar


//The general app bar for every screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KindlyTopAppBar(
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2196F3)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    )

}

@Composable
fun Dashboard(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .verticalScroll(scrollState)
    )
    {
        //Kindly Top App Bar
        KindlyTopAppBar(navController)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            val viewModel: KindlyViewModel = hiltViewModel()



            ActivePromisesCard(viewModel)

            PromisesDueForTheWeekCard(navController)

            RecentActivityCard()

            ImpactSummaryCard()

        }
    }
}

@Composable
fun ActivePromisesCard(viewModel: KindlyViewModel) {
    val totalPromises = viewModel.promises.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Active Promises",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = totalPromises.value.size.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 64.sp,
                        color = Color(0xFF2196F3)
                    )
                    Text(
                        text = "Ongoing Commitments",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.handshake),
                    contentDescription = "Handshake",
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF2196F3))
                )
            }
        }
    }
}

@Composable
fun PromisesDueForTheWeekCard(navController: NavHostController) {

    val viewModel: KindlyViewModel = hiltViewModel()
    val totalPromises = viewModel.promises.collectAsState()

    val today = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }

    val promisesDueForTheWeek = totalPromises.value.filter { promise ->
        val dueDate = Calendar.getInstance().apply { timeInMillis = promise.dueDate }
        dueDate.after(today) || dueDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("all_promises_screen") },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due for the Week",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    text = "See All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }

            promisesDueForTheWeek.forEach { promise ->
                val dueDate = Calendar.getInstance().apply { timeInMillis = promise.dueDate }
                val daysLeft = (dueDate.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dollar),
                        contentDescription = "dollar",
                        modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF2196F3))
                    )

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = promise.title, fontWeight = FontWeight.Normal, fontSize = 14.sp)
                        Text(
                            text = when {
                                daysLeft == 1L -> "Due in 1 day"
                                daysLeft > 1L -> "Due in $daysLeft days"
                                else -> "Due today"
                            },
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            color = Color(0xFFFF5963)
                        )
                    }
                }
            }
        }
    }
}





@Composable
fun RecentActivityCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Recent Activity",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "check icon",
                    modifier = Modifier.size(40.dp)
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Community Center Support",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Completed yesterday",
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = Color(0xFF249689)
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stopclock),
                    contentDescription = "clock icon",
                    modifier = Modifier.size(40.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Elderly care visit",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Saturday . 2 hours",
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                    )
                }

            }
        }

    }
}

@Composable
fun ImpactSummaryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Impact Summary",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Promises Kept
                Column {
                    Text(
                        text = "45", fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp, color = Color(0xFF2196F3)
                    )
                    Text(
                        text = "Promises Kept",
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp, color = Color(0xFF57636C)
                    )
                }
            }
        }
    }
}

