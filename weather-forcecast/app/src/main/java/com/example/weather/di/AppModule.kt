package com.example.weather.di

import android.content.Context
import com.example.data.converter.NullOnEmptyConverterFactory
import com.example.data.interceptor.CacheInterceptor
import com.example.data.interceptor.ForceCacheInterceptor
import com.example.data.repository.WeatherRepositoryImpl
import com.example.data.service.WeatherService
import com.example.domain.repository.WeatherRepository
import com.example.weather.BuildConfig
import com.example.weather.BuildConfig.BASE_URL
import com.example.weather.util.CACHE_MAX_AGE
import com.example.weather.util.CACHE_MAX_SIZE
import com.example.weather.util.CERTIFICATE
import com.example.weather.util.CONNECT_TIMEOUT
import com.scottyab.rootbeer.RootBeer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Provides
        fun provideCache(@ApplicationContext context: Context) =
            Cache(context.cacheDir, CACHE_MAX_SIZE)

        @Provides
        fun provideRootBeer(@ApplicationContext context: Context) = RootBeer(context)

        @Provides
        @Named("url")
        fun provideUrl(): String = BASE_URL

        @Provides
        fun provideWeatherService(retrofit: Retrofit): WeatherService =
            retrofit.create(WeatherService::class.java)

        @Provides
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            @Named("url") url: String,
        ): Retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideOkHttpClient(
            okHttpClientBuilder: OkHttpClient.Builder,
        ): OkHttpClient {
            return okHttpClientBuilder.build()
        }

        @Provides
        fun provideDefaultOkHttpClientBuilder(
            cache: Cache
        ): OkHttpClient.Builder {
            val certificate = CERTIFICATE.trimIndent().decodeCertificatePem()
            val certificates =
                HandshakeCertificates.Builder()
                    .addTrustedCertificate(certificate)
                    .build()

            val builder = OkHttpClient.Builder()
                .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .addNetworkInterceptor(CacheInterceptor(CACHE_MAX_AGE))
                .addInterceptor(ForceCacheInterceptor(CACHE_MAX_AGE))

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    level = (HttpLoggingInterceptor.Level.HEADERS)
                    builder.addInterceptor(this)
                }
            }
            return builder
        }
    }
}
