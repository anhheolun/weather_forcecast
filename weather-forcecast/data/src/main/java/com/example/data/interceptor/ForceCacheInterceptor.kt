package com.example.data.interceptor

import com.example.data.repository.RemoteRepository.Companion.BAD_GATEWAY
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ForceCacheInterceptor(private val maxStale: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().apply {
            val builder: Request.Builder = newBuilder()
            if (this.method.equals("GET", true)) {
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(maxStale, TimeUnit.SECONDS)
                    .build()
                builder.cacheControl(cacheControl)
            }
            val response = chain.proceed(builder.build())
            if (response.code != BAD_GATEWAY) {
                return response
            }
            response.close()
            return chain.proceed(this.newBuilder().build())
        }
    }
}