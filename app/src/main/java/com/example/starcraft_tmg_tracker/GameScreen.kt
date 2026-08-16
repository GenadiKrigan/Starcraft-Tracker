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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

@Composable
fun GameScreen(
    totalRounds: Int,
    startingSupply: Int,
    supplyIncrease: Int,
    onEndGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // מפעילים את מניעת כיבוי המסך!
    KeepScreenOn()
    // 1. משתני הזיכרון (State) - שומרים את הנתונים של כל המשחק
    var currentRound by remember { mutableIntStateOf(1) } //מתחילים מסיבוב 1

    //נתונים של השחקן הכחול
    var blueVp by remember { mutableIntStateOf(0) }
    var blueSupply by remember { mutableIntStateOf(startingSupply) }

    //נתונים של השחקן הכחול
    var redVp by remember { mutableIntStateOf(0) }
    var redSupply by remember { mutableIntStateOf(startingSupply) }

    // המקסימום הרגיל המותר לאספקה
    var currentNormalMaxSupply by remember { mutableIntStateOf(startingSupply) }

    // בדיקה חכמה: האם אנחנו כרגע בסיבוב האחרון
    val isLastRound = currentRound == totalRounds

    //קובעים את המקסימום הנוכחי: אם סיבוב אחרון נשים מספר ענק כדי ש"לא תהיה הגבלה", אחרת 15
    val currentMaxSupply = if (isLastRound) 999 else currentNormalMaxSupply

    // 2. מבנה המסך הראשי - עמודה שמסדרת הכל מלמעלה למטה
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()), // <--- הוספנו את רשת הביטחון כאן!
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly // מרווח את האלמנטים בצורה שווה מלעלה למטה
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
            // 3. קריאה למונה הסיבובים שיצרנו (יופיע למעלה כי הוא ראשון בעמודה)
            RoundCounter(
                currentRound = currentRound,
                roundMax = totalRounds,
                onRoundIncrease = {
                    if(currentRound < totalRounds){
                        currentRound++
                        currentNormalMaxSupply += supplyIncrease
                        // מוסיפים לשני השחקנים את תוספת האספקה שהוגדרה מראש
                        blueSupply += supplyIncrease
                        redSupply += supplyIncrease
                    }
                    // מוודאים שהאספקה לא עוברת את המקסימום (בודקים כבר לפי הסיבוב החדש)
                    if (currentRound < totalRounds) {
                        if (blueSupply > currentNormalMaxSupply) blueSupply = currentNormalMaxSupply
                        if (redSupply > currentNormalMaxSupply) redSupply = currentNormalMaxSupply
                    }
                },
                onRoundDecrease = { if (currentRound > 1){
                    currentRound--
                    currentNormalMaxSupply -= supplyIncrease
                    blueSupply -= supplyIncrease
                    redSupply -= supplyIncrease
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
        GameScreen(
            totalRounds = 5,
            startingSupply = 3,
            supplyIncrease = 1,
            onEndGameClick = {}
        )
    }
}