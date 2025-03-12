package com.loeth.kindly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.loeth.kindly.ui.navigation.BottomNavigationBar

@Composable
fun NotificationScreen(navController: NavHostController){
    Scaffold(
        topBar = { KindlyTopAppBar(navController, "Notification") },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
    Box(modifier = Modifier.fillMaxSize().padding(it)){
        Text(
            "No Notifications Yet!",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge
        )
    }
    }
}
