package com.amirnlz.stylora.pages.dashboard

import com.amirnlz.stylora.pages.dashboard.data.DashboardRepositoryImpl
import com.amirnlz.stylora.pages.dashboard.domain.repo.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository
}