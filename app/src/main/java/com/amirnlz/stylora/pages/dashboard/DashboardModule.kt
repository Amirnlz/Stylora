package com.amirnlz.stylora.pages.dashboard

import android.content.Context
import com.amirnlz.stylora.pages.dashboard.data.DashboardRepositoryImpl
import com.amirnlz.stylora.pages.dashboard.data.data_source.DashboardRemoteDataSource
import com.amirnlz.stylora.pages.dashboard.data.data_source.DashboardRemoteDataSourceImpl
import com.amirnlz.stylora.pages.dashboard.data.service.DashboardApiService
import com.amirnlz.stylora.pages.dashboard.domain.repo.DashboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    @Provides
    @Singleton
    fun providesDashboardApiService(
        retrofit: Retrofit,
    ): DashboardApiService {
        return retrofit.create(DashboardApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardRemoteDataSource(
        dashboardApiService: DashboardApiService,
        @ApplicationContext context: Context
    ): DashboardRemoteDataSource {
        return DashboardRemoteDataSourceImpl(dashboardApiService, context)
    }

    @Provides
    fun bindDashboardRepository(
        remoteDataSource: DashboardRemoteDataSource
    ): DashboardRepository {
        return DashboardRepositoryImpl(remoteDataSource)
    }


}