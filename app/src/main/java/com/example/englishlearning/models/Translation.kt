package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("DictionaryName")
    val dictionaryName: String,
    @SerializedName("Heading")
    val heading: String,
    @SerializedName("OriginalWord")
    val originalWord: String,
    @SerializedName("SoundName")
    val soundName: String,
    @SerializedName("Translation")
    val translation: String,
    @SerializedName("Type")
    val type: Int
)