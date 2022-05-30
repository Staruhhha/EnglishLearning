package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class CompletedAuditions(
    @SerializedName("auditionId")
    val auditionId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("score")
    val score: Boolean,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("audition")
    val audition: Audition
)