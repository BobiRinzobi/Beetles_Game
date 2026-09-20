package com.phoenix.beetles.feature.authors.presentation.presenter

import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.domain.repository.AuthorsRepository
import com.phoenix.beetles.feature.authors.domain.usecase.GetAuthorsUseCase

class AuthorsViewModel(
    private val getAuthorsUseCase: GetAuthorsUseCase
) : ViewModel() {

    fun getAuthors(): List<Author> {
        return getAuthorsUseCase()
    }
}