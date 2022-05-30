package com.example.englishlearning.services

import com.example.englishlearning.models.Minicard
import retrofit2.Call
import retrofit2.http.*

interface DictionaryRequest {

    @POST("authenticate")
    fun auth(@HeaderMap headers : Map<String, String>) : Call<String>

    @GET("Minicard")
    fun wordTranslate(@Query("text") text : String, @Query("srcLang") srcLang : String,
                      @Query("dstLang") dstLang : String,
                      @HeaderMap headers: Map<String, String>) : Call<Minicard>


    @GET("Sound")
    fun getTranscription(@Query("dictionaryName") dictionaryName : String,
                         @Query("fileName") fileName : String,
                         @HeaderMap headers: Map<String, String>) : Call<String>

}