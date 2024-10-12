package com.david.glez.firestorageadvanced.data

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storageMetadata
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await

class StorageService @Inject constructor(private val storage: FirebaseStorage) {
    fun basicExample() {
        Firebase.storage
        val reference = Firebase.storage.reference.child("")
        reference.name // name.png
        reference.path // path/name.png
        reference.bucket // bucket name
    }

    fun uploadBasicImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)
    }

    suspend fun readBasicImage(userId: String): Uri {
        //val reference = storage.reference.child("$userId/profile.png")
        val reference = storage.reference.child("20240927_024609image2215984057681218659.jpg")
        reference.downloadUrl.addOnSuccessListener { Log.i("aris", "success") }
            .addOnFailureListener { Log.i("aris", "fail") }

        return reference.downloadUrl.await()
    }

    suspend fun uploadAndDownloadImage(uri: Uri): Uri {
        //Test read metadata
        /* readCompleteMetadata()
         return Uri.EMPTY*/
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child("download/${uri.lastPathSegment}")
            reference.putFile(uri, createMetadata()).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot, cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
            cancellableContinuation.resume(uri)
        }.addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }

    private fun removeImage(): Boolean {
        val reference = storage.reference.child("download/METADATA4352783564132211675.jpg")
        return reference.delete().isSuccessful
    }

    private suspend fun readMetadataBasic() {
        val reference = storage.reference.child("download/METADATA4352783564132211675.jpg")
        val response = reference.metadata.await()
        val metaInfo = response.getCustomMetadata("key")
        Log.i("keyMetaInfo", "metaInfo: $metaInfo")
    }

    private suspend fun readCompleteMetadata() {
        val reference = storage.reference.child("download/METADATA4352783564132211675.jpg")
        val response = reference.metadata.await()

        response.customMetadataKeys.forEach { key ->
            response.getCustomMetadata(key)?.let { value ->
                Log.i("keyMetaInfo", "key: $key, value: $value")
            }
        }
    }

    private fun createMetadata(): StorageMetadata {
        val metadata = storageMetadata {
            contentType = "image/jpeg"
            setCustomMetadata("date", "11-03-1999")
            setCustomMetadata("key", "value")
        }
        return metadata
    }

    private fun uploadImageWithProgress(uri: Uri) {
        val reference = storage.reference.child("loquesea/miImagen.jpg")
        reference.putFile(uri)
            .addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                Log.i("imageProgress", "Upload is $progress% done")
            }
    }

    private suspend fun getAllImages(): List<Uri> {
        val reference = storage.reference.child("download")
        /*reference.listAll().addOnSuccessListener { result ->
            result.items.forEach {
                Log.i("imageProgress", "image: ${it.name}")
            }
        }*/

        return reference.listAll().await().items.map { it.downloadUrl.await() }
    }
}