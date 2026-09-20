package com.phoenix.beetles.feature.authors.presentation.ui

import android.widget.ListView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.presentation.adapter.AuthorsAdapter

@Composable
fun AuthorsScreen(
    authors: List<Author>
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            ListView(context).apply {
                adapter = AuthorsAdapter(context, authors)
            }
        }
    )
}