package com.phoenix.beetles.feature.authors.presentation.ui

import android.widget.ListView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel

@Composable
fun AuthorsScreen(
    viewModel: AuthorsViewModel
) {
    val authors = viewModel.getAuthors()

    LazyColumn {
        items(authors) { author ->
            AuthorRow(author = author)
        }
    }
}

@Composable
private fun AuthorRow(author: Author) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(author.photoResId),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = author.name,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}