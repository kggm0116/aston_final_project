package ru.kggm.presentation.utility.activity_result_managers

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class TakePhotoManager(private val fragment: Fragment) {
    private var onTaken: (Uri) -> Unit = { }
    private var preInsertedImageUri: Uri? = null
    private val launcher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data.let { uri ->
                when {
                    uri != null -> onTaken(uri)
                    else -> {
                        preInsertedImageUri?.let { onTaken(it) }
                            ?: Log.e("TakePhotoManager", "Unable to retrieve uri of photo taken")
                    }
                }
            }
        }
    }

    fun takePhoto(onTaken: (Uri) -> Unit) {
        this.onTaken = onTaken
        preInsertedImageUri = fragment.requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
        )
        launcher.launch(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, preInsertedImageUri)
        )
    }
}