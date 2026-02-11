package com.woo.peton.domain.model

import android.os.Parcelable
import com.woo.peton.core.utils.LocalDateTimeParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.time.LocalDateTime

@Parcelize
@TypeParceler<LocalDateTime, LocalDateTimeParceler>
data class ReportPost(
    val id: String = "",
    val petID : String = "",
    val reportType: ReportType,
    val title: String,

    val animalType: String = "개",
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
    val updatedAt: LocalDateTime? = null,

    val authorName: String,
    val authorId: String = "",
    val authorProfileImageUrl: String? = null,
    val commentCount: Int = 0,

    val isResolved: Boolean = false
) : Parcelable