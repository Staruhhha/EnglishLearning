package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class PossibleAnswer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("value")
    val value: String
)