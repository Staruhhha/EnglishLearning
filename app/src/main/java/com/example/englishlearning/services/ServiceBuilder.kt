package com.example.englishlearning.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL = "https://www.mptenglishlearning.ru/api/"
    private const val URLDictionary = "https://developers.lingvolive.com/api/v1/"

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttp = OkHttpClient.Builder().addInterceptor(logger).apply {
        addInterceptor(MyInterceptor())
    }.build()

    private val okHttpDictionary = OkHttpClient.Builder().addInterceptor(logger).build()


    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp)

    private val builderDictionary = Retrofit.Builder().baseUrl(URLDictionary)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpDictionary)

    val retrofit = builder.build()

    val retrofitDictionary = builderDictionary.build()

    fun <T> buildServices(serviceType:Class<T>):T{
        return retrofit.create(serviceType)
    }

    fun <T> buildServicesDictionary(serviceType:Class<T>) : T{
        return retrofitDictionary.create(serviceType)
    }

}