package com.loeth.kindly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.loeth.kindly.ui.AddPromise
import com.loeth.kindly.ui.AllPromises
import com.loeth.kindly.ui.Dashboard
import com.loeth.kindly.ui.PromiseDetail

//The routes for each screen
sealed class Screen(val route: String){
    data object Dashboard : Screen("dashboard")
    data object AddPromise : Screen("add_promise")
    data object AllPromises : Screen("all_promises")
    data object PromiseDetails : Screen("promise_detail/{promiseId}"){
        fun createRoute(promiseId: String) = "promise_detail/$promiseId"
    }
}

@Composable
fun KindlyNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        navController.createGraph(
            startDestination = Screen.Dashboard.route
        ){
            composable(Screen.Dashboard.route) { Dashboard() }
            composable(Screen.AddPromise.route) { AddPromise() }
            composable(Screen.AllPromises.route){ AllPromises() }
            composable(Screen.PromiseDetails.route) { PromiseDetail() }
        }
    )
}

