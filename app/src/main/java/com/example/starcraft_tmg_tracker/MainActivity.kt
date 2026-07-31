package com.example.starcraft_tmg_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    var blueVp by remember { mutableStateOf(0) }
    var blueSupply by remember { mutableStateOf(0) }
    val maxSupply = 15

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PlayerCard(
            playerName = "Blue Player",
            playerColor = Color(0xFF2196F3),
            vpValue = blueVp,
            onVpIncrease = { blueVp++ },
            onVpDecrease = { if (blueVp > 0) blueVp-- },
            supplyCurrent = blueSupply,
            supplyMax = maxSupply,
            onSupplyIncrease = { if (blueSupply < maxSupply) blueSupply++ }, // מוגבל עד המקסימום
            onSupplyDecrease = { if (blueSupply > 0) blueSupply-- } // מוגבל עד לאפס
        )
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
            .padding(12.dp)
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = playerName,
                fontSize = 28.sp,
                color = playerColor,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = playerColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(text = "VICTORY POINTS (VP)", fontSize = 18.sp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                FilledIconButton(
                    onClick = onVpDecrease,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 32.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$vpValue",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = playerColor
                )

                FilledIconButton(
                    onClick = onVpIncrease,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 32.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "SUPPLY", fontSize = 18.sp, color = Color.Gray)

            // שורת כפתורים חדשה גם ל-Supply
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                FilledIconButton(
                    onClick = onSupplyDecrease,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 32.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$supplyCurrent / $supplyMax",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                FilledIconButton(
                    onClick = onSupplyIncrease,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 32.sp, color = playerColor, fontWeight = FontWeight.Bold)
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
    card(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ){
            //כותרת המונה

        }
    }
}

// ---------------------------------------------------------
// תצוגה מקדימה (Preview) - מאפשר לראות את העיצוב בלי להריץ על טלפון
// ---------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    StarcraftTMGTrackerTheme {
        GameScreen()
    }
}