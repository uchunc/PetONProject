package com.woo.peton.core.data.datasource

interface ImageDataSource {
    suspend fun uploadImage(uriString: String, path: String): Result<String>
}