package com.example.intellicube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intellicube.ui.theme.IntelliCubeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntelliCubeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IntelliCubeApp()
                }
            }
        }
    }
}

@Composable
fun IntelliCubeApp() {
    val navController = rememberNavController()

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
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntelliCubeTheme {
        Greeting("Android")
    }
}*/