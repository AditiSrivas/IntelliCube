package com.example.intellicube

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTimerClick: () -> Unit,
    onMusicClick: () -> Unit,
    onDocumentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IntelliCube") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hi User, Welcome to IntelliCube",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onTimerClick
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.accesstime),
                        contentDescription = "Timer",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Set Timer",
                        fontSize = 18.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onMusicClick
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.musicnote),
                        contentDescription = "Study Music",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Study Music",
                        fontSize = 18.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDocumentsClick
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.accesstime),
                        contentDescription = "Documents",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "View Documents",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom navigation bar represented as a row of icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Stay on home */ }) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = onMusicClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.musicnote),
                        contentDescription = "Music",
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = onDocumentsClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.description),
                        contentDescription = "Documents",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}