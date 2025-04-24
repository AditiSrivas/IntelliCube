package com.example.disproject

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context)
    }
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var renderedPages by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }
    var searchText by remember {
        mutableStateOf("")
    }
    var searchResults by remember {
        mutableStateOf(emptyList<SearchResults>())
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            renderedPages = pdfBitmapConverter.pdfToBitmaps(uri)
        }
    }

    val choosePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        pdfUri = it
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documents") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (pdfUri != null) {
                        IconButton(onClick = onNavigateToQuiz) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_quiz),
                                contentDescription = "Take Quiz"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if(pdfUri == null) {
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = {
                        choosePdfLauncher.launch("application/pdf")
                    }) {
                        Text(text = "Choose PDF")
                    }

                    Text(
                        text = "Upload a PDF to view content and access the quiz",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(renderedPages) { index, page ->
                        PdfPage(
                            page = page,
                            searchResults = searchResults.find { it.page == index }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            choosePdfLauncher.launch("application/pdf")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Choose another PDF")
                    }

                    Button(
                        onClick = onNavigateToQuiz,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Take Quiz")
                    }
                }

                if(Build.VERSION.SDK_INT >= 35) {
                    OutlinedTextField(
                        value = searchText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Search in PDF") },
                        trailingIcon = {
                            if(searchText.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchText = ""
                                    searchResults = emptyList()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        onValueChange = { newSearchText ->
                            searchText = newSearchText

                            pdfBitmapConverter.renderer?.let { renderer ->
                                scope.launch(Dispatchers.Default) {
                                    searchResults = (0 until renderer.pageCount).map { index ->
                                        renderer.openPage(index).use { page ->
                                            val results = page.searchText(newSearchText)

                                            val matchedRects = results.map {
                                                it.bounds.first()
                                            }

                                            SearchResults(
                                                page = index,
                                                results = matchedRects
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier,
    searchResults: SearchResults? = null
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
            .drawWithContent {
                drawContent()

                val scaleFactorX = size.width / page.width
                val scaleFactorY = size.height / page.height

                searchResults?.results?.forEach { rect ->
                    val adjustedRect = RectF(
                        rect.left * scaleFactorX,
                        rect.top * scaleFactorY,
                        rect.right * scaleFactorX,
                        rect.bottom * scaleFactorY
                    )

                    drawRoundRect(
                        color = Color.Yellow.copy(alpha = 0.5f),
                        topLeft = Offset(
                            x = adjustedRect.left,
                            y = adjustedRect.top
                        ),
                        size = Size(
                            width = adjustedRect.width(),
                            height = adjustedRect.height()
                        ),
                        cornerRadius = CornerRadius(5.dp.toPx())
                    )
                }
            }
    )
}