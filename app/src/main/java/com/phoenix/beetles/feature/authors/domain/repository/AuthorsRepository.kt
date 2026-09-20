package com.phoenix.beetles.feature.authors.domain.repository

import com.phoenix.beetles.feature.authors.domain.model.Author

interface AuthorsRepository {

    fun getAuthors(): List<Author>
}