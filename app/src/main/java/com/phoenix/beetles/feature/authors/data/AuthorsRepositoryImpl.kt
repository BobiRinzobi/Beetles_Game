package com.phoenix.beetles.feature.authors.data

import com.phoenix.beetles.R
import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.domain.repository.AuthorsRepository

class AuthorsRepositoryImpl : AuthorsRepository {

    override fun getAuthors(): List<Author> {
        return listOf(
            Author(
                "Владислав",
                R.drawable.author_vlad
            ),
            Author(
                "Данила",
                R.drawable.author_danila
            )
        )
    }
}