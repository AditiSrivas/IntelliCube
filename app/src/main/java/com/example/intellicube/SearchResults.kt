package com.example.intellicube

import android.graphics.RectF

data class SearchResults(
    val page: Int,
    val results: List<RectF>
)