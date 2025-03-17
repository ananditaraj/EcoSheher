package com.example.ecosheher.cloudinary

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager

object CloudinaryHelper {

    private var isIntitialized = false
    //ohm nom shiva
    fun initCloudinary(context: Context) {
        if (!isIntitialized) {
            val config: HashMap<String, String> = hashMapOf(
                "cloud_name" to "dpq5lcmeb",
                "api_key" to "678449188523326",
                "api_secret" to "3z9_IVZ6VNYdL2F9E4itNDCJglE"
            )
            try {
                MediaManager.init(context, config)
                isIntitialized = true
            } catch (e: Exception) {
                Log.e("Cloudinary", "Cloudinary initialization failed: ${e.message}")
            }

        }
    }
    fun uploadImage(context: Context, imageUri: Uri, onSuccess:  (String) -> Unit, onFailure: (Exception) -> Unit) {

        if (!isIntitialized) initCloudinary(context)

        MediaManager.get().upload(imageUri)
            .callback(object : com.cloudinary.android.callback.UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val imageUrl = resultData?.get("secure_url") as? String
                    if (imageUrl != null) {
                        onSuccess(imageUrl)
                    } else {
                        onFailure(Exception("Failed to retrieve image URL"))
                    }
                }

                override fun onError(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {
                    Log.e("Cloudinary Upload", "Upload failed: ${error?.description}")
                    onFailure(Exception(error?.description ?: "Upload failed"))
                }

                override fun onReschedule(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {
                }
            })
            .dispatch()

    }
}

