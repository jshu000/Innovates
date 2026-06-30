package com.jshu.innovates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jshu.innovates.ui.pan.PanScreen
import com.jshu.innovates.ui.theme.InnovatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InnovatesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "pan_screen") {
                    composable("pan_screen") {
                        PanScreen(onFinish = { finish() })
                    }
                }
            }
        }
    }
}
