package com.kakaopay.kakaopaywork.di

import com.kakaopay.kakaopaywork.domain.SearchBookRepositoryImpl
import com.kakaopay.kakaopaywork.domain.SearchBookUseCase
import com.kakaopay.kakaopaywork.domain.SearchBookUseCaseImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideUseCase(searchBookRepositoryImpl: SearchBookRepositoryImpl): SearchBookUseCase {
        return SearchBookUseCaseImp(searchBookRepositoryImpl)
    }
}
