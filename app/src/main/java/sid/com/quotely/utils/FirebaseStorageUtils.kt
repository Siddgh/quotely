package sid.com.quotely.utils

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import sid.com.quotely.constants.FirebaseConstants

object FirebaseStorageUtils {
    private val firebaseStorageInstance by lazy {
        FirebaseStorage.getInstance()
    }

    fun getMoviePosterReferenceById(id: String, type: String): StorageReference {
        return if (id.isNotEmpty() && id.isNotBlank() && type.isNotBlank() && type.isNotEmpty()) {
            firebaseStorageInstance.getReference(FirebaseConstants.posterStorageReference)
                .child(type)
                .child(StringUtils.getJPGFileName(id))
        } else {
            firebaseStorageInstance.getReference(FirebaseConstants.posterStorageReference)
        }
    }
}