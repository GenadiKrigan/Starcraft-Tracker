package com.example.starcraft_tmg_tracker

import android.content.Context
import androidx.core.content.edit

// 1. "חבילת נתונים": מחלקה שנועדה לארוז את כל נתוני המשחק כדי שיהיה נוח להעביר אותם
data class SavedGameState(
    val totalRounds: Int,
    val currentRound: Int,
    val startingSupply: Int,
    val supplyIncrease: Int,
    val currentNormalMaxSupply: Int,
    val blueVp: Int,
    val blueSupply: Int,
    val redVp: Int,
    val redSupply: Int
)

// 2. אובייקט (מחלקה מיוחדת שקיימת פעם אחת) שמנהל את הגישה לזיכרון
object GameStorage {
    // השם של "הפנקס" שלנו בזיכרון של הטלפון
    private const val PREFS_NAME = "StarcraftGameMemory"

    // פונקציה א': בודקת האם בכלל קיים משחק שמור בפנקס
    fun hasGameSaved(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("hasSavedGame", false)
    }

    // פונקציה ב': כותבת את כל הנתונים העדכניים לתוך הפנקס
    fun saveGame(
        context: Context,
        totalRounds: Int, currentRound: Int,
        startingSupply: Int, supplyIncrease: Int, currentNormalMaxSupply: Int,
        blueVp: Int, blueSupply: Int,
        redVp: Int, redSupply: Int
    ){
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // משתמשים ב-edit כדי לפתוח את הפנקס לכתיבה
        prefs.edit().apply {
            putBoolean("hasSavedGame", true) // מסמנים שיש משחק שמור
            putInt("totalRounds", totalRounds)
            putInt("currentRound", currentRound)
            putInt("startingSupply", startingSupply)
            putInt("supplyIncrease", supplyIncrease)
            putInt("currentNormalMaxSupply", currentNormalMaxSupply)
            putInt("blueVp", blueVp)
            putInt("blueSupply", blueSupply)
            putInt("redVp", redVp)
            putInt("redSupply", redSupply)
            apply() // שומר את הנתונים ברקע וסוגר את הפנקס
        }
    }

    // פונקציה ג': קוראת את הנתונים מהפנקס ומחזירה אותם ארוזים ב"חבילה"
    fun loadGame(context: Context): SavedGameState {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return SavedGameState(
            // המספר השני (למשל 5) הוא ערך ברירת מחדל למקרה שאין כלום בזיכרון
            totalRounds = prefs.getInt("totalRounds", 5),
            currentRound = prefs.getInt("currentRound", 1),
            startingSupply = prefs.getInt("startingSupply", 3),
            supplyIncrease = prefs.getInt("supplyIncrease", 1),
            currentNormalMaxSupply = prefs.getInt("currentNormalMaxSupply", 3),
            blueVp = prefs.getInt("blueVp", 0),
            blueSupply = prefs.getInt("blueSupply", 3),
            redVp = prefs.getInt("redVp", 0),
            redSupply = prefs.getInt("redSupply", 3)
        )
    }

    // פונקציה ד': קורעת את הדפים מהפנקס (מחיקת המשחק הנוכחי)
    fun clearGame(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}