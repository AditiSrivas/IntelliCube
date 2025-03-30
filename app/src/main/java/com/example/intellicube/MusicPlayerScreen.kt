package com.example.intellicube

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.IOException

data class MusicTrack(
    val title: String,
    val artist: String,
    val duration: String,
    val resourceId: Int? = null,
    val fileName: String? = null
)
data class BackgroundImage(
    val name: String,
    val path: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentTrackIndex by remember { mutableStateOf(0) }
    var selectedBackgroundIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // MediaPlayer instance
    val mediaPlayer = remember { MediaPlayer() }
    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }

    // Music tracks
    val tracks = remember {
        listOf(
            MusicTrack("Focus Meditation", "Ambient Sounds", "01:24", fileName = "Music/music1.mp3"),
            MusicTrack("Deep Concentration", "Study Music", "10:34", fileName = "Music/music2.mp3"),
            MusicTrack("Ambient Study", "Background Sounds", "10:24", fileName = "Music/music3.mp3"),
            MusicTrack("Nature Sounds", "Relaxation", "01:25", fileName = "Music/music4.mp3"),
            MusicTrack("Classical Study", "Mozart", "20:10", fileName = "Music/music5.mp3"),
            MusicTrack("Focus Meditation", "Ambient Sounds", "07:41", fileName = "Music/music6.mp3"),
            MusicTrack("Deep Concentration", "Study Music", "07:20", fileName = "Music/music7.mp3"),
            MusicTrack("Ambient Study", "Background Sounds", "02:19", fileName = "Music/music8.mp3"),
            MusicTrack("Nature Sounds", "Relaxation", "02:31", fileName = "Music/music9.mp3"),
            MusicTrack("Classical Study", "Mozart", "10:05", fileName = "Music/music10.mp3")
        )
    }
    // Background images
    val backgroundImages = remember {
        listOf(
            BackgroundImage("Mountains", "Background/bg1.jpg"),
            BackgroundImage("Forest", "Background/bg2.jpg"),
            BackgroundImage("Ocean", "Background/bg3.jpg"),
            BackgroundImage("Space", "Background/bg4.jpg"),
            BackgroundImage("Mountains", "Background/bg5.jpg"),
            BackgroundImage("Forest", "Background/bg6.jpg"),
            BackgroundImage("Ocean", "Background/bg7.jpg"),
            BackgroundImage("Space", "Background/bg8.jpg"),
            BackgroundImage("Ocean", "Background/bg9.jpg"),
            BackgroundImage("Space", "Background/bg10.jpg"),
        )
    }

    val currentTrack = tracks.getOrNull(currentTrackIndex) ?: tracks.first()
    val currentBackground = backgroundImages.getOrNull(selectedBackgroundIndex) ?: backgroundImages.first()

    fun loadBackgroundImageFromAssets(context: Context, fileName: String): androidx.compose.ui.graphics.ImageBitmap? {
        return try {
            context.assets.open(fileName).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun playTrack(trackIndex: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        val track = tracks[trackIndex]
        try {
            val assetFileDescriptor = track.fileName?.let { fileName ->
                context.assets.openFd(fileName)
            }

            if (assetFileDescriptor != null) {
                mediaPlayer.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying = true

                mediaPlayer.setOnCompletionListener {
                    if (currentTrackIndex < tracks.size - 1) {
                        currentTrackIndex++
                        playTrack(currentTrackIndex)
                    } else {
                        isPlaying = false
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exceptions (file not found, etc.)
            e.printStackTrace()
            isPlaying = false
        }
    }

    val backgroundBitmap = remember(selectedBackgroundIndex) {
        loadBackgroundImageFromAssets(context, currentBackground.path)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Music") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image
            backgroundBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = "Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f // Semi-transparent
                )
            }

            // Content
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Currently playing track info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentTrack.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentTrack.artist,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Playback controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MusicControlButton(
                                painter = painterResource(id = R.drawable.skipprevious),
                                contentDescription = "Previous",
                                onClick = {
                                    if (currentTrackIndex > 0) {
                                        currentTrackIndex--
                                        if (isPlaying) {
                                            playTrack(currentTrackIndex)
                                        }
                                    }
                                }
                            )

                            MusicControlButton(
                                painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.playarrow),
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                onClick = {
                                    if (isPlaying) {
                                        mediaPlayer.pause()
                                        isPlaying = false
                                    } else {
                                        if (mediaPlayer.isPlaying.not()) {
                                            playTrack(currentTrackIndex)
                                        } else {
                                            mediaPlayer.start()
                                            isPlaying = true
                                        }
                                    }
                                },
                                size = 64.dp
                            )

                            MusicControlButton(
                                painter = painterResource(id = R.drawable.skipnext),
                                contentDescription = "Next",
                                onClick = {
                                    if (currentTrackIndex < tracks.size - 1) {
                                        currentTrackIndex++
                                        if (isPlaying) {
                                            playTrack(currentTrackIndex)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                // Track list
                Text(
                    text = "Available Tracks",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(tracks) { track ->
                        MusicTrackItem(
                            track = track,
                            isSelected = track == currentTrack,
                            onClick = {
                                val newIndex = tracks.indexOf(track)
                                currentTrackIndex = newIndex
                                playTrack(newIndex)
                            }
                        )
                    }
                }

                // Background options
                Text(
                    text = "Background Options",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    backgroundImages.forEachIndexed { index, bg ->
                        BackgroundOption(
                            modifier = Modifier.weight(1f),
                            index = index,
                            isSelected = index == selectedBackgroundIndex,
                            context = context,
                            imagePath = bg.path,
                            onSelect = {
                                selectedBackgroundIndex = index
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MusicControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    painter: Painter
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicTrackItem(
    track: MusicTrack,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.title,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = track.artist,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = track.duration,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MusicControlButton(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.6f)
            )
        } else if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundOption(
    modifier: Modifier = Modifier,
    index: Int,
    isSelected: Boolean,
    context: Context,
    imagePath: String,
    onSelect: (Int) -> Unit
) {
    val imageBitmap = remember {
        try {
            context.assets.open(imagePath).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            null
        }
    }

    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f),
        onClick = { onSelect(index) },
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Background ${index + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${index + 1}")
                }
            }
        }
    }
}