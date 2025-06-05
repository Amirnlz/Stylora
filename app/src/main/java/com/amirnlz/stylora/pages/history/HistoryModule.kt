package com.amirnlz.stylora.pages.history

import com.amirnlz.stylora.pages.history.data.HistoryRepositoryImpl
import com.amirnlz.stylora.pages.history.domain.repo.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryModule {

    @Binds
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository
}