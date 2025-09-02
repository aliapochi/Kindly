package com.loeth.kindly.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.loeth.kindly.BannerAd
import com.loeth.kindly.R


data class BottomNavItem(
    val name: String,
    val icon: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Dashboard",
        icon = R.drawable.dashboard,
        route = Screen.Dashboard.route
    ),
    BottomNavItem(
        name = "Add Promise",
        icon = R.drawable.addpromise,
        route = Screen.AddPromise.route
    ),
    BottomNavItem(
        name = "All Promises",
        icon = R.drawable.allpromise,
        route = Screen.AllPromises.route
    )
)

@Composable
fun BottomNavigationBar(navController: NavHostController){
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        //Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp, modifier = Modifier.padding(bottom = 4.dp))
        // Banner Ad at the top of Bottom Navigation
        BannerAd(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(4.dp)
                .background(Color.White) // Keeping the container color
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.AllPromises.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(24.dp),
                        tint = if (isSelected) Color(0xFF87CEEB) else Color.Gray
                    )
                    Text(
                        text = item.name,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

}