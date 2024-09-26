package com.david.glez.firestorageadvanced.data

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

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
}