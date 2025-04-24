package com.example.disproject

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.disproject.ui.theme.DISProjectTheme

// App-wide state management for media player
object AppState {
    var mediaPlayer: MediaPlayer? = null
    val isPlaying = mutableStateOf(false)
    val currentTrackIndex = mutableStateOf(0)
    val selectedBackgroundIndex = mutableStateOf(0)

    fun initMediaPlayer(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
            isPlaying.value = false
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppState.initMediaPlayer(this)

        setContent {
            DISProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IntelliCubeApp()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (AppState.isPlaying.value) {
            AppState.mediaPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppState.isPlaying.value && AppState.mediaPlayer?.isPlaying == false) {
            AppState.mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppState.releaseMediaPlayer()
    }
}

@Composable
fun IntelliCubeApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        if (AppState.mediaPlayer == null) {
            AppState.initMediaPlayer(context)
        }

        onDispose { }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onTimerClick = { navController.navigate("timer") },
                onMusicClick = { navController.navigate("music") },
                onDocumentsClick = { navController.navigate("documents") }
            )
        }
        composable("timer") {
            TimerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("music") {
            MusicPlayerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("documents") {
            PdfViewerScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToQuiz = { navController.navigate("quiz") }
            )
        }
        composable("quiz") {
            QuizScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}