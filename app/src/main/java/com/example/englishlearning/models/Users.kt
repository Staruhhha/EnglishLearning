package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("surname")
    val surname: String = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("levelLanguageId")
    val levelLanguageId: String? = "",
    @SerializedName("login")
    val login: String? = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("testId")
    val testId: Int = 0
)