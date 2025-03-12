package com.loeth.kindly.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        modifier = Modifier.fillMaxWidth().background(Color.Red),
        verticalArrangement = Arrangement.Bottom // Ensures items stick to the bottom
    ) {
        BannerAd(modifier = Modifier.fillMaxWidth())
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = Color.Green,
        ) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.name,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.name,
                            fontSize = 12.sp // Ensures proper scaling
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.AllPromises.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}