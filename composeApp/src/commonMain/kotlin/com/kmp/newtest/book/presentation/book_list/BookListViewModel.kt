@file:OptIn(FlowPreview::class)

package com.kmp.newtest.book.presentation.book_list

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.newtest.book.data.repository.DefaultBookRepository
import com.kmp.newtest.book.domain.Book
import com.kmp.newtest.book.domain.BookRepository
import com.kmp.newtest.core.domain.onError
import com.kmp.newtest.core.domain.onSuccess
import com.kmp.newtest.core.presentation.toUiText
import io.github.aakira.napier.Napier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Presentation -> Domain <- Data
class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val cachedBooksList = emptyList<Book>()
    private var searchJob: Job? = null


    private val _state = MutableStateFlow(BookListState())
    val state = _state.onStart {
        if (cachedBooksList.isEmpty()) {
            observeSearchQuery()
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value
    )

    private val books = (1..100).map {
        Book(
            id = it.toString(),
            title = "Book $it",
            imageUrl = "https://test.com",
            authors = listOf("Philipp Lackner"),
            description = "Description $it",
            languages = emptyList(),
            firstPublishYear = null,
            averageRating = 4.67854,
            ratingCount = 5,
            numPages = 100,
            numEditions = 3
        )
    }

    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnBookClick -> {}
            is BookListAction.OnSearchQueryChange -> {
                Napier.d(tag = "jhlee", message = "viewnodel : ${action.query}")
                _state.update {
                    it.copy(searchQuery = action.query, searchResults = books, isLoading = false)
                }
            }

            is BookListAction.OnTabSelected -> {
                Napier.d(tag = "jhlee", message = "OnTabSelected : ${action.index}")
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun observeSearchQuery() {
        state.map {
            it.searchQuery
        }.distinctUntilChanged().debounce(500).onEach { query ->
            when {
                query.isBlank() -> {
                    _state.update { it.copy(errorMessage = null, searchResults = cachedBooksList) }
                }

                query.length >= 2 -> {
                    searchJob?.cancel()
                    searchJob = searchBooks(query)

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        bookRepository.searchBook(query).onSuccess { searchResults ->
            _state.update {
                it.copy(
                    isLoading = false, errorMessage = null, searchResults = searchResults
                )
            }
        }.onError { error ->
            _state.update {
                it.copy(
                    searchResults = emptyList(), isLoading = false, errorMessage = error.toUiText()
                )
            }
        }
    }
}