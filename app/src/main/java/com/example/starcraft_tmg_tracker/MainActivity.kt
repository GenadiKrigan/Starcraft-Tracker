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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.ui.platform.LocalContext
import com.example.starcraft_tmg_tracker.ui.theme.StarcraftTMGTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // מאפשר לאפליקציה למלא את כל המסך
        setContent {
            // שימוש ב-Theme הייחודי של הפרויקט שלך
            StarcraftTMGTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // קוראים למערכת הניווט במקום למסך בודד
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ---------------------------------------------------------
// הקומפוננטות שלנו
// ---------------------------------------------------------

@Composable
fun GameScreen(
    totalRounds: Int,
    startingSupply: Int,
    supplyIncrease: Int,
    modifier: Modifier = Modifier
) {
    // 1. משתני הזיכרון (State) - שומרים את הנתונים של כל המשחק
    var currentRound by remember {mutableStateOf(1)} //מתחילים מסיבוב 1

    //נתונים של השחקן הכחול
    var blueVp by remember {mutableStateOf(0)}
    var blueSupply by remember { mutableStateOf(startingSupply) }

    //נתונים של השחקן הכחול
    var redVp by remember {mutableStateOf(0)}
    var redSupply by remember { mutableStateOf(startingSupply) }

    // המקסימום הרגיל המותר לאספקה
    val normalMaxSupply = 15
    val maxRound = totalRounds

    // בדיקה חכמה: האם אנחנו כרגע בסיבוב האחרון
    val isLastRound = currentRound == maxRound

    //קובעים את המקסימום הנוכחי: אם סיבוב אחרון נשים מספר ענק כדי ש"לא תהיה הגבלה", אחרת 15
    val currentMaxSupply = if (isLastRound) 999 else normalMaxSupply

    // 2. מבנה המסך הראשי - עמודה שמסדרת הכל מלמעלה למטה
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp), // תופס את כל המסך עם קצת רווח בקצוות
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly // מרווח את האלמנטים בצורה שווה מלעלה למטה
    ) {
        // 3. קריאה למונה הסיבובים שיצרנו (יופיע למעלה כי הוא ראשון בעמודה)
        RoundCounter(
            currentRound = currentRound,
            roundMax = maxRound,
            onRoundIncrease = {
                if(currentRound < maxRound){
                    currentRound++
                    // מוסיפים לשני השחקנים את תוספת האספקה שהוגדרה מראש
                    blueSupply += supplyIncrease
                    redSupply += supplyIncrease
                }
                // מוודאים שהאספקה לא עוברת את המקסימום (בודקים כבר לפי הסיבוב החדש)
                if (currentRound < maxRound) {
                    if (blueSupply > normalMaxSupply) blueSupply = normalMaxSupply
                    if (redSupply > normalMaxSupply) redSupply = normalMaxSupply
                }
                              },
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
                supplyMax = normalMaxSupply,
                onSupplyIncrease = { if (blueSupply < normalMaxSupply) blueSupply++ },
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
                supplyMax = normalMaxSupply,
                onSupplyIncrease = { if (redSupply < normalMaxSupply) redSupply++ },
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
    roundMax: Int,
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
                    text = "$currentRound / $roundMax",
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

//SetupScreen
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
    var rounds by remember { mutableStateOf(5) }
    var startSupply by remember { mutableStateOf(3) }
    var supplyIncrease by remember { mutableStateOf(1) }

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
fun AppNavigation(modifier: Modifier = Modifier) {
    // 1. יצירת ה"נווט" שזוכר באיזה מסך אנחנו נמצאים
    val navController = rememberNavController()

    // 2. מפת המסכים (NavHost) - מתחילים במסך ה-setup
    NavHost(navController = navController, startDestination = "setup") {
        // --- תחנה ראשונה: מסך ההגדרות ---
        composable("setup") {
            SetupScreen(
                onStartGameClick = { rounds, startSupply, supplyIncrease ->
                    // כשהשחקן לוחץ התחל, אנחנו מנווטים למסך המשחק ומעבירים לו את המספרים בתוך הקישור
                    navController.navigate("game/$rounds/$startSupply/$supplyIncrease")
                }
            )
        }

        // --- תחנה שנייה: מסך המשחק הראשי ---
        composable(
            route = "game/{rounds}/{startSupply}/{supplyIncrease}",
            arguments = listOf(
                navArgument("rounds") { type = NavType.IntType },
                navArgument("startSupply") { type = NavType.IntType },
                navArgument("supplyIncrease") { type = NavType.IntType }
            )
        ){ backStackEntry ->
            // חילוץ המספרים שהעברנו מהמסך הקודם
            val rounds = backStackEntry.arguments?.getInt("rounds") ?: 5
            val startSupply = backStackEntry.arguments?.getInt("startSupply") ?: 3
            val supplyIncrease = backStackEntry.arguments?.getInt("supplyIncrease") ?: 1

            // כאן קורה הקסם: נועלים את המסך לרוחב רק כשאנחנו בתוך מסך המשחק!
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            // הפעלת מסך המשחק עם הנתונים החדשים
            GameScreen(
                totalRounds = rounds,
                startingSupply = startSupply,
                supplyIncrease = supplyIncrease
            )
        }
    }
}

// ---------------------------------------------------------
// תצוגה מקדימה (Preview) - מאפשר לראות את העיצוב בלי להריץ על טלפון
// ---------------------------------------------------------
@Preview(showBackground = true, widthDp = 800, heightDp = 400) // הגדרנו רוחב גדול שמדמה Landscape
/*@Composable
fun GameScreenPreview() {
    StarcraftTMGTrackerTheme {
        GameScreen()
    }
}*/

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SetupScreenPreview() {
    StarcraftTMGTrackerTheme { // שים לב שזה ה-Theme הייחודי של הפרויקט שלך
        SetupScreen(
            onStartGameClick = { rounds, startSupply, supplyIncrease ->
                // זוהי רק תצוגה מקדימה, אז אנחנו משאירים את הפעולה ריקה כרגע
            }
        )
    }
}