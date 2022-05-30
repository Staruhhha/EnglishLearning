package com.example.englishlearning.services

import com.example.englishlearning.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiRequest {

    @GET("tests/random")
    fun getTestRandom() : Call<Tests>

    @POST("users")
    fun addUser(@Body newUser : Users) : Call<Users>

    @POST("users/Authorize")
    fun authUser(@Body user : Users) : Call<Users>

    @GET("users/{id_user}/NoCompleted/Auditions/Android")
    fun getAudAv(@Path("id_user") id_user : Int) : Call<ArrayList<Auditions>>

    @GET("users/{id_user}/NoCompleted/Readings")
    fun getReadingsAv(@Path("id_user") id_user : Int) : Call<ArrayList<Readings>>

    @GET("auditions/{id_aud}")
    fun getAudition(@Path("id_aud") id_aud : Int) : Call<Auditions>

    @GET("Readings/{id_read}")
    fun getReading(@Path("id_read") id_read : Int) : Call<Readings>

    @GET("users/{id_user}")
    fun getUserById(@Path("id_user") id_user : Int) : Call<Users>

    @GET("Auditions/{id_aud}/GetAudioLink")
    fun getAuditionTask(@Path("id_aud") id_aud : Int) : Call<Links>

    @POST("auditions/{id_aud}/DisposeAudioLink")
    fun disposeAudition(@Path("id_aud") id_aud: Int) : Call<Unit>

    @POST("CompletedAuditions")
    fun completedAuditionAdd(@Body compAud : CompletedAuditions) : Call<CompletedAuditions>

    @POST("CompletedReadings")
    fun completedReadingAdd(@Body comReading : CompletedReadings) : Call<CompletedReadings>

    @GET("users/{user_id}/Completed/Auditions/Android")
    fun getCompletedAud(@Path("user_id") user_id : Int) : Call<ArrayList<CompletedAuditions>>

    @GET("users/{user_id}/Completed/Readings")
    fun getCompletedReading(@Path("user_id") user_id: Int) : Call<ArrayList<CompletedReadings>>

}