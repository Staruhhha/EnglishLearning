package com.example.englishlearning.models


import com.google.gson.annotations.SerializedName

data class PossibleAnswersInQuestion(
    @SerializedName("id")
    val id: Int,
    @SerializedName("possibleAnswer")
    val possibleAnswer: PossibleAnswer,
    @SerializedName("possibleAnswerId")
    val possibleAnswerId: Int,
    @SerializedName("questionId")
    val questionId: Int
)