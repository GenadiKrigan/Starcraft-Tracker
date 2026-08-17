package com.example.starcraft_tmg_tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

@Composable
fun GameScreen(
    onEndGameClick: () -> Unit,
    modifier: Modifier = Modifier,
    totalRounds: Int = 5,
    startingSupply: Int = 3,
    supplyIncrease: Int = 1,
    isResume: Boolean = false,
) {
    val context = LocalContext.current

    // 1. שואבים את נתוני המשחק מהפנקס שלנו (רק אם אנחנו במצב 'המשך משחק')
    val savedState = remember { if (isResume) GameStorage.loadGame(context) else null }

    // 2. הגדרת המשתנים - עכשיו הם שואלים: "יש שמירה? ניקח ממנה! אין שמירה? נתחיל מחדש!"
    var currentRound by remember { mutableIntStateOf(savedState?.currentRound ?: 1) }

    var blueVp by remember { mutableIntStateOf(savedState?.blueVp ?: 0) }
    var blueSupply by remember { mutableIntStateOf(savedState?.blueSupply ?: startingSupply) }

    var redVp by remember { mutableIntStateOf(savedState?.redVp ?: 0) }
    var redSupply by remember { mutableIntStateOf(savedState?.redSupply ?: startingSupply) }

    var currentNormalMaxSupply by remember { mutableIntStateOf(savedState?.currentNormalMaxSupply ?: startingSupply) }

    // 3. חוקי המשחק הבסיסיים (לוקחים מהשמירה אם קיימת)
    val activeTotalRounds = savedState?.totalRounds ?: totalRounds
    val activeSupplyIncrease = savedState?.supplyIncrease ?: supplyIncrease
    val activeStartingSupply = savedState?.startingSupply ?: startingSupply

    val isLastRound = currentRound == activeTotalRounds
    val currentMaxSupply = if (isLastRound) 999 else currentNormalMaxSupply

    // 4. הקסם: שמירה אוטומטית!
    LaunchedEffect(currentRound, blueVp, blueSupply, redVp, redSupply, currentNormalMaxSupply) {
        GameStorage.saveGame(
            context = context,
            totalRounds = activeTotalRounds,
            currentRound = currentRound,
            startingSupply = activeStartingSupply,
            supplyIncrease = activeSupplyIncrease,
            currentNormalMaxSupply = currentNormalMaxSupply,
            blueVp = blueVp,
            blueSupply = blueSupply,
            redVp = redVp,
            redSupply = redSupply
        )
    }

    // מפעילים את מניעת כיבוי המסך!
    KeepScreenOn()

    // (המשתנים הישנים והכפולים שהיו כאן - נמחקו!)

    // 2. מבנה המסך הראשי - עמודה שמסדרת הכל מלמעלה למטה
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            // כפתור החזרה - מיושר לשמאל
            TextButton(
                onClick = onEndGameClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 50.dp)
            ) {
                Text("< BACK", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            // 3. קריאה למונה הסיבובים
            RoundCounter(
                currentRound = currentRound,
                roundMax = activeTotalRounds, // <--- תוקן ל-activeTotalRounds
                onRoundIncrease = {
                    if(currentRound < activeTotalRounds){ // <--- תוקן ל-activeTotalRounds
                        currentRound++
                        currentNormalMaxSupply += activeSupplyIncrease // <--- תוקן ל-active
                        blueSupply += activeSupplyIncrease
                        redSupply += activeSupplyIncrease
                    }
                    if (currentRound < activeTotalRounds) {
                        if (blueSupply > currentNormalMaxSupply) blueSupply = currentNormalMaxSupply
                        if (redSupply > currentNormalMaxSupply) redSupply = currentNormalMaxSupply
                    }
                },
                onRoundDecrease = {
                    if (currentRound > 1){
                        currentRound--
                        currentNormalMaxSupply -= activeSupplyIncrease // <--- תוקן ל-active
                        blueSupply -= activeSupplyIncrease
                        redSupply -= activeSupplyIncrease
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // 4. שורה שמחלקת את המסך לשניים - שחקן כחול מול שחקן אדום (יופיעו מתחת למונה)
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            // 5 א. קריאה לכרטיס של השחקן הכחול
            PlayerCard(
                playerName = "Blue Player",
                playerColor = Color(0xFF2196F3), // קוד צבע כחול
                vpValue = blueVp,
                onVpIncrease = { blueVp++ },
                onVpDecrease = { if (blueVp > 0) blueVp-- },
                supplyCurrent = blueSupply,
                supplyMax = currentMaxSupply,
                onSupplyIncrease = { if (blueSupply < currentNormalMaxSupply) blueSupply++ },
                onSupplyDecrease = { if (blueSupply > 0) blueSupply-- },
                modifier = Modifier.weight(1f) //מחלק את המקום בשורה שווה בשווה
            )
            // 5ב. קריאה לכרטיס של השחקן האדום (ממש ליד הכחול)
            PlayerCard(
                playerName = "Red Player",
                playerColor = Color(0xFFE53935), // קוד צבע אדום
                vpValue = redVp,
                onVpIncrease = { redVp++ },
                onVpDecrease = { if (redVp > 0) redVp-- },
                supplyCurrent = redSupply,
                supplyMax = currentMaxSupply,
                onSupplyIncrease = { if (redSupply < currentNormalMaxSupply) redSupply++ },
                onSupplyDecrease = { if (redSupply > 0) redSupply-- },
                modifier = Modifier.weight(1f) // לוקח בדיוק את אותו משקל (מקום) כמו הכחול
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400) // הגדרנו רוחב גדול שמדמה Landscape
@Composable
fun GameScreenPreview() {
    StarcraftTMGTrackerTheme {
        // הוספתי נתוני דמה כדי שהתצוגה המקדימה תעבוד
        GameScreen(onEndGameClick = {}
        )
    }
}