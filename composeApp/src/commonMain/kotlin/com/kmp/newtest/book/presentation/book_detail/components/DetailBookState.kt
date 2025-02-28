package com.kmp.newtest.book.presentation.book_detail.components

import com.kmp.newtest.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true, val isFavorite: Boolean = false, val book: Book? = null
)