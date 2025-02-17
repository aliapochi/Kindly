package com.loeth.kindly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.loeth.kindly.data.PromiseDatabase
import com.loeth.kindly.ui.navigation.KindlyNavGraph
import com.loeth.kindly.ui.theme.KindlyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: PromiseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KindlyTheme {
                KindlyNavGraph()
            }
        }
    }
}

