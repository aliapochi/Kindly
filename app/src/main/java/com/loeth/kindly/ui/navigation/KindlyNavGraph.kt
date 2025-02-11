package com.loeth.kindly.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.loeth.kindly.KindlyViewModel
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
    val viewModel: KindlyViewModel = hiltViewModel()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
    ) { innerPadding ->

    NavHost(
        navController = navController,
        navController.createGraph(
            startDestination = Screen.Dashboard.route
        )
        {
            composable(Screen.Dashboard.route) { Dashboard() }
            composable(Screen.AddPromise.route) {
                AddPromise(viewModel)
            }
            composable(Screen.AllPromises.route){ AllPromises(viewModel, navController) }
            composable(Screen.PromiseDetails.createRoute(promiseId = "{promiseId}")) { backStackEntry ->
                val promiseId = backStackEntry.arguments?.getString("promiseId")
                PromiseDetail(promiseId!!, viewModel) }
        },
        modifier = Modifier.padding(innerPadding)
    )
    }
}

