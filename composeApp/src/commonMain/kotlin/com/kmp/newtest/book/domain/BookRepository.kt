package com.kmp.newtest.book.domain

import com.kmp.newtest.core.domain.DataError
import com.kmp.newtest.core.domain.Result

interface BookRepository {
    suspend fun searchBook(query: String): Result<List<Book>, DataError.Remote>
}