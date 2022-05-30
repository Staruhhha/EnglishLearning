package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Tests(
    @SerializedName("id")
    val id: Int,
    @SerializedName("testQuestions")
    val testQuestions: ArrayList<TestQuestion>
)