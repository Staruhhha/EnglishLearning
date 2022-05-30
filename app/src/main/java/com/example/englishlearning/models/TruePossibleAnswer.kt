package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class TruePossibleAnswer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("value")
    val value: String
)