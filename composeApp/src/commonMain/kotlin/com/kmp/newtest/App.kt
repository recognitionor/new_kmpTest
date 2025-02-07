package com.kmp.newtest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kmp.newtest.book.data.network.KtorRemoteBookDataSource
import com.kmp.newtest.book.data.repository.DefaultBookRepository
import com.kmp.newtest.book.presentation.book_list.BookListScreenRoot
import com.kmp.newtest.book.presentation.book_list.BookListViewModel
import com.kmp.newtest.core.data.HttpClientFactory
import io.ktor.client.engine.HttpClientEngine
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(engine: HttpClientEngine) {
    BookListScreenRoot(viewModel = remember {
        BookListViewModel(
            bookRepository = DefaultBookRepository(
                remoteBookDataSource = KtorRemoteBookDataSource(httpClient = HttpClientFactory.create(engine))
            )
        )
    }, onBookClick = {

    })
}