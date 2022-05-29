package com.example.carsapp.di

import com.example.carsapp.features.data.data_source.ApiRepository
import com.example.carsapp.features.data.reository.RequestRepoImpl
import com.example.carsapp.features.domain.repo.RequestRepo
import com.example.carsapp.features.domain.use_cases.GetCars
import com.example.carsapp.features.domain.use_cases.RequestUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val BASE_URL = "https://demo1585915.mockable.io/api/v1/"
        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(18, TimeUnit.SECONDS)
            .writeTimeout(18, TimeUnit.SECONDS)
            .readTimeout(18, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            })
        val client = clientBuilder.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRepo(apiRepository: ApiRepository): RequestRepo {
        return RequestRepoImpl(apiRepository = apiRepository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(requestRepo: RequestRepo): RequestUseCase {
        return RequestUseCase(GetCars(requestRepo))
    }

    @Provides
    @Singleton
    fun provideApiRepo(retrofit: Retrofit): ApiRepository {
        return retrofit.create(ApiRepository::class.java)
    }


}