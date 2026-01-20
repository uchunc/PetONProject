package com.woo.peton.domain.model

import java.time.LocalDateTime

data class ReportPost(
    val id: String = "",
    val petID : String = "",
    val reportType: ReportType,
    val title: String,

    val breed: String,
    val gender: String,
    val age: String,

    val content: String,
    val imageUrl: String = "",

    val latitude: Double,
    val longitude: Double,
    val locationDescription: String,

    val occurrenceDate: LocalDateTime,
    val createdAt: LocalDateTime,

    val authorName: String,
    val authorId: String = "",
    val commentCount: Int = 0,

    val isResolved: Boolean = false
)