package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class TestQuestion(
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: Question,
    @SerializedName("questionId")
    val questionId: Int,
    @SerializedName("testId")
    val testId: Int
)