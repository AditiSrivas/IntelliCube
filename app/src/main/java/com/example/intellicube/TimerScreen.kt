package com.example.intellicube

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class TimerMode {
    STUDY, BREAK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMode by remember { mutableStateOf(TimerMode.STUDY) }
    var timerRunning by remember { mutableStateOf(false) }
    var studyDuration by remember { mutableStateOf(10.minutes) }
    var breakDuration by remember { mutableStateOf(10.minutes) }
    var remainingTime by remember { mutableStateOf(studyDuration) }

    var showSettingsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(timerRunning, currentMode) {
        if (timerRunning) {
            while (remainingTime.inWholeSeconds > 0) {
                delay(1000)
                remainingTime -= 1.seconds
            }

            // Timer finished
            if (remainingTime.inWholeSeconds <= 0) {
                // Switch modes
                currentMode = if (currentMode == TimerMode.STUDY) {
                    remainingTime = breakDuration
                    TimerMode.BREAK
                } else {
                    remainingTime = studyDuration
                    TimerMode.STUDY
                }
            }
        }
    }

    if (showSettingsDialog) {
        TimerSettingsDialog(
            studyMinutes = studyDuration.inWholeMinutes.toInt(),
            breakMinutes = breakDuration.inWholeMinutes.toInt(),
            onDismiss = { showSettingsDialog = false },
            onConfirm = { studyMins, breakMins ->
                studyDuration = studyMins.minutes
                breakDuration = breakMins.minutes

                // Reset the timer with new duration if not running
                if (!timerRunning) {
                    remainingTime = if (currentMode == TimerMode.STUDY) studyDuration else breakDuration
                }

                showSettingsDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (currentMode == TimerMode.STUDY) "Study Time" else "Break Time",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentMode == TimerMode.STUDY) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer display
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = remainingTime.inWholeSeconds.toFloat() /
                            if (currentMode == TimerMode.STUDY)
                                studyDuration.inWholeSeconds.toFloat()
                            else
                                breakDuration.inWholeSeconds.toFloat(),
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 12.dp,
                    color = if (currentMode == TimerMode.STUDY)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = formatTime(remainingTime.inWholeSeconds),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        timerRunning = !timerRunning
                    },
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (timerRunning) R.drawable.pause else R.drawable.playarrow),
                        contentDescription = if (timerRunning) "Pause" else "Start",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Button(
                    onClick = {
                        // Reset timer
                        timerRunning = false
                        remainingTime = if (currentMode == TimerMode.STUDY) studyDuration else breakDuration
                    },
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "Reset", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showSettingsDialog = true }
            ) {
                Text("Set Timer")
            }
        }
    }
}

@Composable
fun TimerSettingsDialog(
    studyMinutes: Int,
    breakMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (studyMinutes: Int, breakMinutes: Int) -> Unit
) {
    var studyMins by remember { mutableStateOf(studyMinutes) }
    var breakMins by remember { mutableStateOf(breakMinutes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Timer Settings") },
        text = {
            Column {
                Text("Study Time (minutes)")
                Slider(
                    value = studyMins.toFloat(),
                    onValueChange = { studyMins = it.toInt() },
                    valueRange = 20f..180f,
                    steps = 160,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Text("$studyMins minutes")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Break Time (minutes)")
                Slider(
                    value = breakMins.toFloat(),
                    onValueChange = { breakMins = it.toInt() },
                    valueRange = 5f..30f,
                    steps = 25,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Text("$breakMins minutes")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(studyMins, breakMins) }
            ) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

// Helper function to format time
fun formatTime(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}