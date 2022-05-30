package com.example.englishlearning.services

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("ApiKey", "AndroidMishaS12345")
            .build()
    return chain.proceed(request)
    }

}