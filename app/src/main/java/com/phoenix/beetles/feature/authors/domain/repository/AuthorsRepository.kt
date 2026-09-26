package com.phoenix.beetles.feature.authors.domain.repository

import com.phoenix.beetles.feature.authors.domain.entity.Author

interface AuthorsRepository {

    fun getAuthors(): List<Author>
}