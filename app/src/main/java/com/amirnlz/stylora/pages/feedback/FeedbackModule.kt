package com.amirnlz.stylora.pages.feedback

import android.content.Context
import com.amirnlz.stylora.pages.feedback.data.data_source.FeedbackRemoteDataSource
import com.amirnlz.stylora.pages.feedback.data.data_source.FeedbackRemoteDataSourceImpl
import com.amirnlz.stylora.pages.feedback.data.service.FeedbackApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    fun provideFeedbackApiService(retrofit: Retrofit): FeedbackApiService {
        return retrofit.create(FeedbackApiService::class.java)
    }

    @Provides
    fun provideFeedbackDataSource(
        apiService: FeedbackApiService,
        @ApplicationContext context: Context
    ): FeedbackRemoteDataSource {
        return FeedbackRemoteDataSourceImpl(
            apiService = apiService,
            context = context
        )
    }
}