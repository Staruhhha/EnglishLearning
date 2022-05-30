package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Minicard(
    @SerializedName("Heading")
    val heading: String? = "",
    @SerializedName("SeeAlso")
    val seeAlso: List<String>? = null,
    @SerializedName("SourceLanguage")
    val sourceLanguage: Int? = 0,
    @SerializedName("TargetLanguage")
    val targetLanguage: Int? = 0,
    @SerializedName("Translation")
    val translation: Translation? = null
)