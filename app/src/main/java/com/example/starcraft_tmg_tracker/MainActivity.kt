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
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // מאפשר לאפליקציה למלא את כל המסך
        setContent {
            // שימוש ב-Theme הייחודי של הפרויקט שלך
            StarcraftTMGTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // קוראים למערכת הניווט במקום למסך בודד
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // 1. יצירת ה"נווט" שזוכר באיזה מסך אנחנו נמצאים
    val navController = rememberNavController()

    // 2. מפת המסכים (NavHost) - מתחילים במסך ה-setup
    NavHost(navController = navController, startDestination = "setup", modifier = modifier) {
        // --- תחנה ראשונה: מסך ההגדרות ---
        composable("setup") {
            SetupScreen(
                onStartGameClick = { rounds, startSupply, supplyIncrease ->
                    // כשהשחקן לוחץ התחל, אנחנו מנווטים למסך המשחק ומעבירים לו את המספרים בתוך הקישור
                    navController.navigate("game/$rounds/$startSupply/$supplyIncrease")
                }
            )
        }

        // --- תחנה שנייה: מסך המשחק הראשי ---
        composable(
            route = "game/{rounds}/{startSupply}/{supplyIncrease}",
            arguments = listOf(
                navArgument("rounds") { type = NavType.IntType },
                navArgument("startSupply") { type = NavType.IntType },
                navArgument("supplyIncrease") { type = NavType.IntType }
            )
        ){ backStackEntry ->
            // חילוץ המספרים שהעברנו מהמסך הקודם
            val rounds = backStackEntry.arguments?.getInt("rounds") ?: 5
            val startSupply = backStackEntry.arguments?.getInt("startSupply") ?: 3
            val supplyIncrease = backStackEntry.arguments?.getInt("supplyIncrease") ?: 1

            // כאן קורה הקסם: נועלים את המסך לרוחב רק כשאנחנו בתוך מסך המשחק!
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            // הפעלת מסך המשחק עם הנתונים החדשים
            GameScreen(
                totalRounds = rounds,
                startingSupply = startSupply,
                supplyIncrease = supplyIncrease
            )
        }
    }
}