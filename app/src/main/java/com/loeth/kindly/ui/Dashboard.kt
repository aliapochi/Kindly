package com.loeth.kindly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loeth.kindly.R
import com.loeth.kindly.ui.theme.KindlyTheme


//The general app bar for every screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KindlyTopAppBar(
    onAddClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = "My Promises",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        },
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Promise", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2196F3)
        )
    )

}

@Composable
fun Dashboard() {
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
        KindlyTopAppBar {}
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            ActivePromisesCard()

            PromisesDueForTheWeekCard()

            RecentActivityCard()

            ImpactSummaryCard()

        }
    }
}

@Composable
fun ActivePromisesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
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
                        text = "00",
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
fun PromisesDueForTheWeekCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
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
                    text = "Due for the Week",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Text(
                    text = "View All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
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
                    Text(text = "Monthly Support", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                    Text(
                        text = "Due in 2 days . $250",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFFFF5963)
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.belief),
                    contentDescription = "belief",
                    modifier = Modifier.size(40.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Food Bank Volunteer",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Saturday . 2 hours",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF57636C)
                    )
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
        shape = RoundedCornerShape(8.dp)
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
                    fontSize = 24.sp
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
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Completed yesterday",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
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
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Saturday . 2 hours",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
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
        shape = RoundedCornerShape(8.dp)
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
                    fontSize = 24.sp
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Promises Kept
                Column() {
                    Text(
                        text = "45", fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp, color = Color(0xFF2196F3)
                    )
                    Text(
                        text = "Promises Kept",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp, color = Color (0xFF57636C)
                    )
                }
                //Total Support
                Column() {
                    Text(
                        text = "$2.5k", fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp, color = Color(0xFF39D2C0)
                    )
                    Text(text = "Total Support", fontWeight = FontWeight.Normal,
                        fontSize = 12.sp, color = Color (0xFF57636C))
                }
                //People helped
                Column() {
                    Text(
                        text = "32",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp,
                        color = Color(0xFFEE8B60)
                    )
                    Text(text = "People Helped", fontWeight = FontWeight.Normal,
                        fontSize = 12.sp, color = Color (0xFF57636C))
                }

            }
        }
    }
}

@Preview
@Composable
fun DashboardPreview() {
    KindlyTheme {
        Dashboard()
    }
}