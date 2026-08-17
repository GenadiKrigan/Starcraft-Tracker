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
import androidx.compose.ui.platform.LocalContext
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
    val navController = rememberNavController()
    val context = LocalContext.current // הבאנו את ה-context כדי שנוכל למחוק את המשחק השמור

    // שינוי: ה-startDestination הוא עכשיו מסך הפתיחה
    NavHost(navController = navController, startDestination = "start", modifier = modifier) {

        // --- תחנה 1: מסך הבית (החדש) ---
        composable("start") {
            StartScreen(
                onNewGameClick = {
                    // כפתור משחק חדש: מוחקים את השמירה הישנה מהפנקס, ועוברים להגדרות!
                    GameStorage.clearGame(context)
                    navController.navigate("setup")
                },
                onResumeGameClick = {
                    // כפתור המשך משחק: מדלגים על ההגדרות והולכים ישר למשחק
                    navController.navigate("game/resume")
                }
            )
        }

        // --- תחנה 2: מסך ההגדרות ---
        composable("setup") {
            SetupScreen(
                onStartGameClick = { rounds, startSupply, supplyIncrease ->
                    // הוספנו את המילה 'new' לכתובת
                    navController.navigate("game/new/$rounds/$startSupply/$supplyIncrease")
                }
            )
        }

        // --- תחנה 3: המשך משחק קיים (Resume) ---
        composable("game/resume") {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            GameScreen(
                isResume = true, // זה אומר למסך המשחק למשוך את הנתונים מהזיכרון
                onEndGameClick = { navController.popBackStack("start", inclusive = false) }
            )
        }

        // --- תחנה 4: התחלת משחק חדש (לאחר ההגדרות) ---
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
                isResume = false, // אנחנו מתחילים מאפס, לא מהזיכרון
                onEndGameClick = { navController.popBackStack("start", inclusive = false) }
            )
        }
    }
}