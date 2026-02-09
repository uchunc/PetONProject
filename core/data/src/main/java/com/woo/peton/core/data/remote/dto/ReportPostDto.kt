package com.woo.peton.core.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class ReportPostDto(
    @DocumentId
    val id: String = "",

    val reportType: String = "",
    val title: String = "",

    val animalType: String = "",
    val breed: String = "",
    val gender: String = "",
    val age: String = "",

    val content: String = "",
    val imageUrl: String = "",
    val geoLocation: GeoPoint? = null,
    val locationDescription: String = "",

    val occurrenceDate: Timestamp? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,

    val authorName: String = "",
    val authorId: String = "",
    val commentCount: Int = 0,

    val isResolved: Boolean = false
)

