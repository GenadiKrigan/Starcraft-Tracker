package com.example.starcraft_tmg_tracker

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .padding(4.dp) // הוקטן מ-8
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp) // הוקטן מ-12 כדי לחסוך מקום
                .fillMaxWidth()
        ) {
            Text(
                text = playerName,
                fontSize = 20.sp, // הוקטן מ-22
                color = playerColor,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = playerColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp) // הוקטן מ-8
            )

            Text(text = "VICTORY POINTS (VP)", fontSize = 12.sp, color = Color.Gray) // הוקטן מ-14

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp) // הוקטן מ-4
            ) {
                FilledIconButton(
                    onClick = onVpDecrease,
                    modifier = Modifier.size(40.dp), // הוקטן מ-48
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$vpValue",
                    fontSize = 48.sp, // הוקטן מ-56
                    fontWeight = FontWeight.Bold,
                    color = playerColor
                )

                FilledIconButton(
                    onClick = onVpIncrease,
                    modifier = Modifier.size(40.dp), // הוקטן מ-48
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp)) // הוקטן מ-8

            Text(text = "SUPPLY", fontSize = 12.sp, color = Color.Gray) // הוקטן מ-14

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp) // הוקטן מ-4
            ) {
                FilledIconButton(
                    onClick = onSupplyDecrease,
                    modifier = Modifier.size(40.dp), // הוקטן מ-48
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }

                if (supplyMax == 999) {
                    // תצוגה לסיבוב האחרון!
                    Text(
                        text = "POWER OVERWHELMING",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, // שמתי גודל קטן יותר כדי שהמשפט הארוך ייכנס לכרטיסייה
                        color = playerColor // צובע את הטקסט בצבע של השחקן לאפקט מגניב!
                    )
                } else {
                    Text(
                        text = "$supplyCurrent / $supplyMax",
                        fontSize = 28.sp, // הוקטן מ-32
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledIconButton(
                    onClick = onSupplyIncrease,
                    modifier = Modifier.size(40.dp), // הוקטן מ-48
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
    roundMax: Int,
    onRoundIncrease: () -> Unit,
    onRoundDecrease: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(4.dp), // הוקטן מ-8
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp) // הוקטן מ-16
        ){
            Text(text = "ROUND", fontSize = 14.sp, color = Color.Gray) // הוקטן מ-16

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 4.dp) // הוקטן מ-8
            ){
                FilledIconButton(onClick = onRoundDecrease, modifier = Modifier.size(36.dp)) { // הקטנו קצת כפתורים גם פה
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text(
                    text = "$currentRound / $roundMax",
                    fontSize = 28.sp, // הוקטן מ-32
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                FilledIconButton(onClick = onRoundIncrease, modifier = Modifier.size(36.dp)) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    // 1. השגת הגישה ל"מסך הפיזי" (ה-Activity) של הטלפון
    val context = LocalContext.current

    // 2. אפקט חד-פעמי שקורה כשהקומפוננטה הזו עולה למסך
    DisposableEffect(orientation){
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}

        // שומרים בזיכרון מה היה המצב הקודם של המסך (כדי שנוכל להחזיר אותו אחר כך)
        val originalOrientation = activity.requestedOrientation

        // 3. משנים את כיוון המסך לכיוון שביקשנו (למשל לרוחב)
        activity.requestedOrientation = orientation

        // 4. מה קורה כשיוצאים מהמסך? (onDispose) - מחזירים את המצב לקדמותו
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current

    // DisposableEffect פועל ברגע שהמסך עולה, ומפעיל את onDispose כשהמסך נסגר
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val window = activity?.window

        // מדליק את "מונע השינה" של המסך
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // כשהמסך הזה ייסגר (נחזור להגדרות), נכבה את מונע השינה כדי לא לגמור למשתמש את הסוללה
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}