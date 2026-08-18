package com.example.starcraft_tmg_tracker

import android.content.Context
import androidx.core.content.edit

// Data class to store all game state information
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

// Singleton to manage game data persistence
object GameStorage {
    // Storage key for SharedPreferences
    private const val PREFS_NAME = "StarcraftGameMemory"

    // Checks if a saved game exists
    fun hasGameSaved(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("hasSavedGame", false)
    }

    // Writes the current game state to storage
    fun saveGame(
        context: Context,
        totalRounds: Int, currentRound: Int,
        startingSupply: Int, supplyIncrease: Int, currentNormalMaxSupply: Int,
        blueVp: Int, blueSupply: Int,
        redVp: Int, redSupply: Int
    ){
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Open storage for editing
        prefs.edit().apply {
            putBoolean("hasSavedGame", true) // Mark that a game is saved
            putInt("totalRounds", totalRounds)
            putInt("currentRound", currentRound)
            putInt("startingSupply", startingSupply)
            putInt("supplyIncrease", supplyIncrease)
            putInt("currentNormalMaxSupply", currentNormalMaxSupply)
            putInt("blueVp", blueVp)
            putInt("blueSupply", blueSupply)
            putInt("redVp", redVp)
            putInt("redSupply", redSupply)
            apply() // Save data asynchronously
        }
    }

    // Reads the game state from storage
    fun loadGame(context: Context): SavedGameState {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return SavedGameState(
            // Defaults used if storage is empty
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

    // Clears the current game data from storage
    fun clearGame(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}