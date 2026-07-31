package com.example.starcraft_tmg_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // מאפשר לאפליקציה למלא את כל המסך
        setContent {
            // שימוש ב-Theme הייחודי של הפרויקט שלך
            StarcraftTMGTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // קריאה למסך שלנו והעברת הריווח (Padding) כדי שהתוכן לא יוסתר תחת שורת הסטטוס
                    GameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ---------------------------------------------------------
// הקומפוננטות שלנו
// ---------------------------------------------------------

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    // 1. משתני הזיכרון (State) - שומרים את הנתונים של כל המשחק
    var currentRound by remember {mutableStateOf(1)}//מתחילים מסיבוב 1

    //נתונים של השחקן הכחול
    var blueVp by remember {mutableStateOf(0)}
    var blueSupply by remember { mutableStateOf(0) }

    //נתונים של השחקן הכחול
    var redVp by remember {mutableStateOf(0)}
    var redSupply by remember { mutableStateOf(0) }

    val maxSupply = 15// המקסימום המותר לאספקה

    // 2. מבנה המסך הראשי - עמודה שמסדרת הכל מלמעלה למטה
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp), // תופס את כל המסך עם קצת רווח בקצוות
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly // מרווח את האלמנטים בצורה שווה מלעלה למטה
    ) {
        // 3. קריאה למונה הסיבובים שיצרנו (יופיע למעלה כי הוא ראשון בעמודה)
        RoundCounter(
            currentRound = currentRound,
            onRoundIncrease = { currentRound++ },
            onRoundDecrease = { if (currentRound > 1) currentRound-- }
        )
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
                supplyMax = maxSupply,
                onSupplyIncrease = { if (blueSupply < maxSupply) blueSupply++ },
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
                supplyMax = maxSupply,
                onSupplyIncrease = { if (redSupply < maxSupply) redSupply++ },
                onSupplyDecrease = { if (redSupply > 0) redSupply-- },
                modifier = Modifier.weight(1f) // לוקח בדיוק את אותו משקל (מקום) כמו הכחול
            )
        }
    }
}

@Composable
fun PlayerCard(
    playerName: String,
    playerColor: Color,
    vpValue: Int,
    onVpIncrease: () -> Unit,
    onVpDecrease: () -> Unit,
    supplyCurrent: Int,
    supplyMax: Int,
    onSupplyIncrease: () -> Unit,
    onSupplyDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = playerName,
                fontSize = 22.sp,
                color = playerColor,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = playerColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(text = "VICTORY POINTS (VP)", fontSize = 14.sp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                FilledIconButton(
                    onClick = onVpDecrease,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$vpValue",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = playerColor
                )

                FilledIconButton(
                    onClick = onVpIncrease,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "SUPPLY", fontSize = 14.sp, color = Color.Gray)

            // שורת כפתורים חדשה גם ל-Supply
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                FilledIconButton(
                    onClick = onSupplyDecrease,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$supplyCurrent / $supplyMax",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                FilledIconButton(
                    onClick = onSupplyIncrease,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RoundCounter(
    currentRound: Int,
    onRoundIncrease: () -> Unit,
    onRoundDecrease: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ){
            //כותרת המונה
            Text(text = "ROUND", fontSize = 16.sp, color = Color.Gray)
            //שורה שמכילה את הפלוס, המינוס והמספר
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 8.dp)
            ){
                //כפתור המינוס
                FilledIconButton(onClick = onRoundDecrease) {
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                //מספר הסיבוב הנוכחי
                Text(
                    text = "$currentRound",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                //כםתור פלוס
                FilledIconButton(onClick = onRoundIncrease) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

        }
    }
}

// ---------------------------------------------------------
// תצוגה מקדימה (Preview) - מאפשר לראות את העיצוב בלי להריץ על טלפון
// ---------------------------------------------------------
@Preview(showBackground = true, widthDp = 800, heightDp = 400) // הגדרנו רוחב גדול שמדמה Landscape
@Composable
fun GameScreenPreview() {
    StarcraftTMGTrackerTheme {
        GameScreen()
    }
}