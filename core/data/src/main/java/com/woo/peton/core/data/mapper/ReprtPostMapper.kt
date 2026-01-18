package com.woo.peton.core.data.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woo.peton.core.data.remote.dto.ReportPostDto
import com.woo.peton.core.utils.toLocalDateTime
import com.woo.peton.core.utils.toTimestamp
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import java.time.LocalDateTime

fun ReportPostDto.toDomain(): MissingPet {
    return MissingPet(
        id = this.id,
        reportType = try { ReportType.valueOf(this.reportType) } catch (e: Exception) { ReportType.MISSING },
        title = this.title,
        breed = this.breed,
        gender = this.gender,
        age = this.age,
        content = this.content,
        imageUrl = this.imageUrl,
        latitude = this.geoLocation?.latitude ?: 0.0,
        longitude = this.geoLocation?.longitude ?: 0.0,
        locationDescription = this.locationDescription,
        occurrenceDate = this.occurrenceDate.toLocalDateTime(),
        createdAt = this.createdAt.toLocalDateTime(),
        authorName = this.authorName,
        authorId = this.authorId,
        commentCount = this.commentCount,
        isResolved = this.isResolved
    )
}

fun MissingPet.toDto(): ReportPostDto {
    return ReportPostDto(
        reportType = this.reportType.name,
        title = this.title,
        breed = this.breed,
        gender = this.gender,
        age = this.age,
        content = this.content,
        imageUrl = this.imageUrl,
        geoLocation = GeoPoint(this.latitude, this.longitude),
        locationDescription = this.locationDescription,
        occurrenceDate = this.occurrenceDate.toTimestamp(),
        createdAt = if (this.createdAt == LocalDateTime.MIN) Timestamp.now() else this.createdAt.toTimestamp(),
        authorName = this.authorName,
        authorId = this.authorId,
        commentCount = this.commentCount,
        isResolved = this.isResolved
    )
}