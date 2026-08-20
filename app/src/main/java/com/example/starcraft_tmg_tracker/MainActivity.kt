package com.example.starcraft_tmg_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.content.pm.ActivityInfo
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView) // Getting the Controller to control the system bars
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // Setting the behavior of the controller
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars()) //  Hiding the notification bar at the top and the navigation buttons at the bottom/side
        setContent {
            // Using the project's custom theme
            StarcraftTMGTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black
                ) { innerPadding ->
                    // Use the navigation system instead of a single screen
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current // Get context to allow clearing the saved game

    NavHost(navController = navController, startDestination = "start", modifier = modifier) {

        // --- 1: Home Screen ---
        composable("start") {
            StartScreen(
                onNewGameClick = {
                    // New game button: Clear old save and navigate to setup
                    GameStorage.clearGame(context)
                    navController.navigate("setup")
                },
                onResumeGameClick = {
                    // Resume game button: Skip setup and go straight to game
                    navController.navigate("game/resume")
                }
            )
        }

        // --- 2: Setup Screen ---
        composable("setup") {
            SetupScreen(
                onStartGameClick = { rounds, startSupply, supplyIncrease ->
                    navController.navigate("game/new/$rounds/$startSupply/$supplyIncrease")
                }
            )
        }

        // --- 3: Resume Existing Game ---
        composable("game/resume") {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            GameScreen(
                isResume = true, // Tell GameScreen to load data from storage
                onEndGameClick = { navController.popBackStack("start", inclusive = false) }
            )
        }

        // --- 4: Start New Game (After Setup) ---
        composable(
            route = "game/new/{rounds}/{startSupply}/{supplyIncrease}",
            arguments = listOf(
                navArgument("rounds") { type = NavType.IntType },
                navArgument("startSupply") { type = NavType.IntType },
                navArgument("supplyIncrease") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val rounds = backStackEntry.arguments?.getInt("rounds") ?: 5
            val startSupply = backStackEntry.arguments?.getInt("startSupply") ?: 3
            val supplyIncrease = backStackEntry.arguments?.getInt("supplyIncrease") ?: 1

            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            GameScreen(
                totalRounds = rounds,
                startingSupply = startSupply,
                supplyIncrease = supplyIncrease,
                isResume = false, // Starting a fresh game
                onEndGameClick = { navController.popBackStack("start", inclusive = false) }
            )
        }
    }
}