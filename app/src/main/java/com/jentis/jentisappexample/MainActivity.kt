package com.jentis.jentisappexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jentis.jentisappexample.fragments.ConfigurationScreen
import com.jentis.jentisappexample.fragments.MainScreen
import com.jentis.jentisappexample.fragments.TrackingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JentisApp()
        }
    }
}

@Composable
fun JentisApp() {
    val navController = rememberNavController()
    NavigationGraph(navController = navController)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") { MainScreen(navController) }
        composable("configuration") { ConfigurationScreen(navController) }
        composable("tracking") { TrackingScreen(navController) }
    }
}
