package com.loeth.kindly.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.ui.AddPromise
import com.loeth.kindly.ui.AllPromises
import com.loeth.kindly.ui.Dashboard
import com.loeth.kindly.ui.NotificationScreen
import com.loeth.kindly.ui.PromiseDetail
import com.loeth.kindly.ui.SplashScreen

//The routes for each screen
sealed class Screen(val route: String){
    data object Dashboard : Screen("dashboard")
    data object AddPromise : Screen("add_promise")
    data object AllPromises : Screen("all_promises")
    data object Notifications : Screen("notifications")
    data object SplashScreen : Screen("splash_screen")
    data object PromiseDetails : Screen("promise_detail/{promiseId}"){
        fun createRoute(promiseId: String) = "promise_detail/$promiseId"
    }
}

@Composable
fun KindlyNavGraph(navController: NavHostController = rememberNavController()){
    val viewModel: KindlyViewModel = hiltViewModel()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.SplashScreen.route) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->

       Row(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                navController.createGraph(
                    startDestination = Screen.SplashScreen.route
                )
                {
                    composable(Screen.Dashboard.route) { Dashboard(navController) }
                    composable(Screen.SplashScreen.route) { SplashScreen(navController) }
                    composable(Screen.AddPromise.route) { AddPromise(viewModel, navController) }
                    composable(Screen.Notifications.route) { NotificationScreen(navController) }
                    composable(Screen.AllPromises.route) { AllPromises(viewModel, navController) }
                    composable(Screen.PromiseDetails.createRoute(promiseId = "{promiseId}")) { backStackEntry ->
                        val promiseId = backStackEntry.arguments?.getString("promiseId")
                        PromiseDetail(
                            promiseId!!,
                            viewModel,
                            onDeleteSuccess = { navController.popBackStack() },
                            navController
                        )
                    }
                }
            )
        }
    }
}

