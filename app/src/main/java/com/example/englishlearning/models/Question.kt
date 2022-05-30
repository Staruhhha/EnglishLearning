package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    val id: Int,
    @SerializedName("possibleAnswersInQuestions")
    val possibleAnswersInQuestions: List<PossibleAnswersInQuestion>,
    @SerializedName("truePossibleAnswer")
    val truePossibleAnswer: PossibleAnswer,
    @SerializedName("truePossibleAnswerId")
    val truePossibleAnswerId: Int,
    @SerializedName("value")
    val value: String
)