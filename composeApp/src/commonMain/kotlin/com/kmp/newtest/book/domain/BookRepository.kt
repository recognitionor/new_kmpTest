package com.kmp.newtest.book.domain

import com.kmp.newtest.book.data.database.BookEntity
import com.kmp.newtest.core.domain.DataError
import com.kmp.newtest.core.domain.EmptyResult
import com.kmp.newtest.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBook(query: String): Result<List<Book>, DataError.Remote>
    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    fun getFavoriteBooks(): Flow<List<Book>>
    fun isBookFavorite(id: String): Flow<Boolean>
    suspend fun makeAsFavorite(book: Book): EmptyResult<DataError.Local>
    suspend fun deleteFromFavorites(id: String)

}