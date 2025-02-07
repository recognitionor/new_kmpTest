package com.kmp.newtest.book.data.repository

import com.kmp.newtest.book.data.mappers.toBook
import com.kmp.newtest.book.data.network.RemoteBookDataSource
import com.kmp.newtest.book.domain.Book
import com.kmp.newtest.book.domain.BookRepository
import com.kmp.newtest.core.domain.DataError
import com.kmp.newtest.core.domain.Result
import com.kmp.newtest.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
) : BookRepository {

    override suspend fun searchBook(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource.searchBooks(query).map {
            it.results.map { dto ->
                dto.toBook()
            }
        }
    }
}