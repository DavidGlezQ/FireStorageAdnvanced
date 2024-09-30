package com.david.glez.firestorageadvanced.data

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
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
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child("download/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
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
}