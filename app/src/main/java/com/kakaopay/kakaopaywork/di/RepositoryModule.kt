package com.kakaopay.kakaopaywork.di

import com.kakaopay.kakaopaywork.domain.SearchBookRepository
import com.kakaopay.kakaopaywork.domain.SearchBookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchBookRepository(repository: SearchBookRepositoryImpl): SearchBookRepository
}