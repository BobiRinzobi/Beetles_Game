package com.phoenix.beetles.feature.authors.presentation.presenter

import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.authors.domain.model.Author
import com.phoenix.beetles.feature.authors.domain.repository.AuthorsRepository

class AuthorsViewModel(
    private val repository: AuthorsRepository
) : ViewModel() {

    fun getAuthors(): List<Author> {
        return repository.getAuthors()
    }
}