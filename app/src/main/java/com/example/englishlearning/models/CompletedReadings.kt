package com.example.englishlearning.models

data class CompletedReadings(
    val id: Int,
    val userId: Int,
    val readingId: Int,
    val score: Boolean,
    val reading: Readings?
)