package com.kmp.newtest.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kmp.newtest.book.data.database.DatabaseFactory
import com.kmp.newtest.book.data.database.FavoriteBookDatabase
import com.kmp.newtest.book.data.network.KtorRemoteBookDataSource
import com.kmp.newtest.book.data.network.RemoteBookDataSource
import com.kmp.newtest.book.data.repository.DefaultBookRepository
import com.kmp.newtest.book.domain.BookRepository
import com.kmp.newtest.book.presentation.SelectedBookViewModel
import com.kmp.newtest.book.presentation.book_detail.BookDetailViewModel
import com.kmp.newtest.book.presentation.book_list.BookListViewModel
import com.kmp.newtest.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModules = module {
    single {
        HttpClientFactory.create(get())
    }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build()
    }

    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)

//    viewModelOf(::Select)


}