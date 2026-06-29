package com.loeth.kindly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.loeth.kindly.data.PromiseDatabase
import com.loeth.kindly.ui.RequestNotificationPermission
import com.loeth.kindly.ui.navigation.KindlyNavGraph
import com.loeth.kindly.ui.theme.KindlyTheme2
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: PromiseDatabase

    private lateinit var inAppUpdateManager: InAppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleDailyMessage(applicationContext)

        inAppUpdateManager = InAppUpdateManager(this)
        inAppUpdateManager.checkForUpdate()

        setContent {
            KindlyTheme2 {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    RequestNotificationPermission()
                }
                KindlyNavGraph()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inAppUpdateManager.resumeUpdate()
    }
}
