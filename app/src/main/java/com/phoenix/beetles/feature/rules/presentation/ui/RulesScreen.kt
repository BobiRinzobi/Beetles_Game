package com.phoenix.beetles.feature.rules.presentation.ui

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun RulesScreen(
    rules: String
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            TextView(context)
        },
        update = { textView ->
            textView.text = HtmlCompat.fromHtml(
                rules,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    )
}