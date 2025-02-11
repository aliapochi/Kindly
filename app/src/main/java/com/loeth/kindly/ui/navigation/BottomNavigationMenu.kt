package com.loeth.kindly.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.loeth.kindly.R

//enum class BottomNavigationItem(val icon: Int, val label: Screen){
//    Dashboard(R.drawable.dashboard, Screen.Dashboard),
//    AddPromise(R.drawable.addpromise, Screen.AddPromise),
//    AllPromise(R.drawable.allpromise, Screen.AllPromises)
//}
//Bottom navigation bar items
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

    NavigationBar{
       bottomNavItems.forEach{ item ->
           NavigationBarItem(
               icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.name)},
               label = { Text(text = item.name)},
               selected = currentRoute == item.route,
               onClick = {
                   if(currentRoute != item.route){
                       navController.navigate(item.route){
                           popUpTo(Screen.AllPromises.route){
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