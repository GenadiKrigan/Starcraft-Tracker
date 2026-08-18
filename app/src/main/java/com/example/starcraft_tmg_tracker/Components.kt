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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

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
            .padding(4.dp)
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = playerName,
                fontSize = 20.sp,
                color = playerColor,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = playerColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(text = "VICTORY POINTS (VP)", fontSize = 12.sp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                FilledIconButton(
                    onClick = onVpDecrease,
                    modifier = Modifier.size(40.dp),
                    enabled = vpValue > 0,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = if(vpValue > 0) playerColor else Color.Gray, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$vpValue",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = playerColor
                )

                FilledIconButton(
                    onClick = onVpIncrease,
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 24.sp, color = playerColor, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "SUPPLY", fontSize = 12.sp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                val isMinusEnabled = supplyCurrent > 0 && supplyMax != 999

                FilledIconButton(
                    onClick = onSupplyDecrease,
                    modifier = Modifier.size(40.dp),
                    enabled = isMinusEnabled,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("-", fontSize = 24.sp, color = if(isMinusEnabled) playerColor else Color.Gray, fontWeight = FontWeight.Bold)
                }

                var textSize by remember { mutableStateOf(14.sp) }
                if (supplyMax == 999) {
                    // Display for the final round
                    Text(
                        text = "Additional Supply Depot required", //"POWER OVERWHELMING",
                        fontWeight = FontWeight.Bold,
                        fontSize = textSize,
                        color = playerColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.hasVisualOverflow){
                                textSize *=0.9f
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                } else {
                    Text(
                        text = "$supplyCurrent / $supplyMax",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val isPlusEnabled = supplyCurrent < supplyMax && supplyMax != 999

                FilledIconButton(
                    onClick = onSupplyIncrease,
                    modifier = Modifier.size(40.dp),
                    enabled = isPlusEnabled,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = playerColor.copy(alpha = 0.1f))
                ) {
                    Text("+", fontSize = 24.sp, color = if(isPlusEnabled) playerColor else Color.Gray, fontWeight = FontWeight.Bold)
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
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ){
            Text(text = "ROUND", fontSize = 14.sp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 4.dp)
            ){
                FilledIconButton(
                    onClick = onRoundDecrease,
                    enabled = currentRound > 1,
                    modifier = Modifier.size(36.dp)) {
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if(currentRound > 1)Color.Black else Color.Gray )
                }
                Text(
                    text = "$currentRound / $roundMax",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                FilledIconButton(
                    onClick = onRoundIncrease,
                    enabled = currentRound < roundMax,
                    modifier = Modifier.size(36.dp)) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if(currentRound < roundMax)Color.Black else Color.Gray)
                }
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    // Get the current Activity
    val context = LocalContext.current

    // Effect that runs when the composable enters the screen
    DisposableEffect(orientation){
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}

        // Save the original orientation to restore it later
        val originalOrientation = activity.requestedOrientation

        // Set the requested orientation
        activity.requestedOrientation = orientation

        // Restore the original orientation when leaving the screen
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context as? Activity
        val window = activity?.window

        // Enable "keep screen on" flag
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Clear flag when leaving the screen to save battery
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}