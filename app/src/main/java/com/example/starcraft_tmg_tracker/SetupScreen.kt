package com.example.starcraft_tmg_tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
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
fun SettingRow(
    label: String,
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(0.9f) // תופס 90% מרוחב המסך
            .padding(vertical = 8.dp)
    ){
        //שם ההגדרה (למשל: "Total Rounds")
        Text(text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )

        //אזור הכפתורים והמספר
        Row(verticalAlignment = Alignment.CenterVertically){
            FilledIconButton(onClick = onDecrease, modifier = Modifier.size(40.dp)) {
                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = "$value",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            FilledIconButton(onClick = onIncrease, modifier = Modifier.size(40.dp)) {
                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// המסך המלא של ההגדרות
@Composable
fun SetupScreen(onStartGameClick: (totalRounds: Int, startingSupply: Int, supplyIncrease: Int) -> Unit){
    // 1. משתני הזיכרון (State) להגדרות ההתחלתיות (ברירת מחדל)
    var rounds by remember { mutableIntStateOf(5) }
    var startSupply by remember { mutableIntStateOf(3) }
    var supplyIncrease by remember { mutableIntStateOf(1) }

    // 2. סידור המסך בטור
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("GAME SETUP", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        // 3. שימוש באבן הבניין שלנו 3 פעמים
        SettingRow(
            label = "Total Rounds",
            value = rounds,
            onDecrease = { if (rounds > 1) rounds-- },
            onIncrease = { rounds++ }
        )

        SettingRow(
            label = "Starting Supply",
            value = startSupply,
            onDecrease = { if (startSupply > 1) startSupply-- },
            onIncrease = { startSupply++ }
        )

        SettingRow(
            label = "Supply Increase Per Round",
            value = supplyIncrease,
            onDecrease = { if (supplyIncrease > 1) supplyIncrease-- },
            onIncrease = { supplyIncrease++ }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 4. כפתור התחלת המשחק
        Button(
            onClick = { onStartGameClick(rounds, startSupply, supplyIncrease) },
            modifier = Modifier.padding(16.dp)
        ){
            Text("START GAME", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SetupScreenPreview() {
    StarcraftTMGTrackerTheme {
        SetupScreen(
            onStartGameClick = { _, _, _ ->
                // זוהי רק תצוגה מקדימה, אז אנחנו משאירים את הפעולה ריקה כרגע
            }
        )
    }
}