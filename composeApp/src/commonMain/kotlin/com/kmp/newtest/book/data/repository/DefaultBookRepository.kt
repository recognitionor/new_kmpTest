package com.kmp.newtest.book.data.repository

import androidx.compose.runtime.collectAsState
import com.kmp.newtest.book.data.database.FavoriteBookDao
import com.kmp.newtest.book.data.mappers.toBook
import com.kmp.newtest.book.data.mappers.toBookEntity
import com.kmp.newtest.book.data.network.RemoteBookDataSource
import com.kmp.newtest.book.domain.Book
import com.kmp.newtest.book.domain.BookRepository
import com.kmp.newtest.core.domain.DataError
import com.kmp.newtest.core.domain.EmptyResult
import com.kmp.newtest.core.domain.Result
import com.kmp.newtest.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
) : BookRepository {

    override suspend fun searchBook(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource.searchBooks(query).map { dto ->
            dto.results.map {
                it.toBook()
            }
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if (localResult == null) {
            remoteBookDataSource.getBookDetails(bookId).map { it.description }
        } else {
            Result.Success(localResult.description)
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao.getFavoriteBooks().map { entity -> entity.map { it.toBook() } }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao.getFavoriteBooks().map { entity ->
            entity.any {
                it.id == id
            }
        }
    }

    override suspend fun makeAsFavorite(book: Book): EmptyResult<DataError.Local> {

        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        return favoriteBookDao.deleteFavoriteBook(id)
    }
}
