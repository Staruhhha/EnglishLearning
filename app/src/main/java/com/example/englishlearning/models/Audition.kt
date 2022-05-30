package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Audition(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("levelLanguageId")
    val levelLanguageId: String,
    @SerializedName("task")
    val task: Any,
    @SerializedName("taskText")
    val taskText: String
)