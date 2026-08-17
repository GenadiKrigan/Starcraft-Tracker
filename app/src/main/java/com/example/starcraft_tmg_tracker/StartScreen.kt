package com.example.starcraft_tmg_tracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onResumeGameClick: () -> Unit
) {
    // משיגים את הגישה לטלפון כדי שנוכל לקרוא מהזיכרון
    val context = LocalContext.current

    // בודקים אם יש משחק שמור - פעולה זו קורית פעם אחת כשהמסך עולה
    val hasSavedGame = remember { GameStorage.hasGameSaved(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // כותרת האפליקציה
        Text(
            text = "StarCraft",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "TMG Tracker",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // כפתור משחק חדש
        Button(
            onClick = onNewGameClick,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(8.dp)
        ) {
            Text(text = "New Game", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }

        // כפתור המשך משחק
        FilledTonalButton(
            onClick = onResumeGameClick,
            enabled = hasSavedGame,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(8.dp)
        ) {
            Text(text = "Resumed Game", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

// תצוגה מקדימה - כדי שנוכל לראות את העיצוב בלי להריץ
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun StartScreenPreview() {
    StartScreen(
        onNewGameClick = {},
        onResumeGameClick = {}
    )
}