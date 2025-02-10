package com.loeth.kindly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.loeth.kindly.data.PromiseDatabase
import com.loeth.kindly.data.PromiseEntity
import com.loeth.kindly.ui.navigation.KindlyNavGraph
import com.loeth.kindly.ui.theme.KindlyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: PromiseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DatabaseCheck", "Database instance in MainActivity: $database")
        enableEdgeToEdge()
        setContent {
            KindlyTheme {
                KindlyNavGraph()
            }
        }
    }
}

