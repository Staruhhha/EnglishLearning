package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Readings(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("levelLanguageId")
    val levelLanguageId: String,
    @SerializedName("task")
    val task: String
)