package com.kmp.newtest.book.data.network

import com.kmp.newtest.book.data.dto.BookWorkDto
import com.kmp.newtest.book.data.dto.SearchResponseDto
import com.kmp.newtest.core.domain.DataError
import com.kmp.newtest.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>


}