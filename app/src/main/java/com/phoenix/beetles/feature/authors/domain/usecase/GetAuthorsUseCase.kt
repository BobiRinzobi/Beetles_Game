package com.phoenix.beetles.feature.authors.domain.usecase

import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.domain.repository.AuthorsRepository


class GetAuthorsUseCase(
    private val repository: AuthorsRepository
) {
    operator fun invoke(): List<Author> {
        return repository.getAuthors()
    }
}