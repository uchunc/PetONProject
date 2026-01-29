package com.woo.peton.core.data.remote.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.woo.peton.core.data.datasource.ImageDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageDataSourceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageDataSource {

    override suspend fun uploadImage(uriString: String, path: String): Result<String> {
        return try {
            val uri = Uri.parse(uriString)
            val ref = storage.reference.child(path)

            ref.putFile(uri).await()

            val downloadUrl = ref.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}