package com.phoenix.beetles.feature.authors.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.phoenix.beetles.feature.authors.domain.model.Author

class AuthorsAdapter(
    private val context: Context,
    private val authors: List<Author>
) : BaseAdapter() {

    override fun getCount(): Int = authors.size

    override fun getItem(position: Int): Author = authors[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
            setPadding(32, 24, 32, 24)
        }

        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                120,
                120
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 32
            }

            textSize = 20f
        }

        val author = authors[position]

        imageView.setImageResource(author.photoResId)
        textView.text = author.name

        row.addView(imageView)
        row.addView(textView)

        return row
    }
}