package com.amirnlz.stylora.pages.feedback.data.data_source

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.feedback.data.service.FeedbackApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class FeedbackRemoteDataSourceImpl @Inject constructor(
    private val apiService: FeedbackApiService,
    private val context: Context
) : FeedbackRemoteDataSource {
    override suspend fun giveFeedback(
        imageUri: Uri,
        feedbackType: String,
        language: String
    ): Response<FeedbackResponse> {
        try {
            val imageFile = uriToFile(context.applicationContext, imageUri, "upload_image")
                ?: throw IllegalArgumentException("Could not convert Uri to File")

            val requestBody =
                imageFile.asRequestBody(
                    context.contentResolver.getType(imageUri)?.toMediaTypeOrNull()
                )
            val imagePart =
                MultipartBody.Part.createFormData("image_file", imageFile.name, requestBody)

            return apiService.postFeedback(
                imageFile = imagePart,
                feedbackType = feedbackType.toLowerCase(Locale.current)
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                deviceId = getAndroidID().toRequestBody("text/plain".toMediaTypeOrNull()),
                language = language.toLowerCase(Locale.current).capitalize(Locale.current)
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            )
        } catch (e: Exception) {
            throw e
        }

    }

    override suspend fun getFeedbackHistory(): Response<List<FeedbackResponse>> {
        try {
            return apiService.getFeedbackHistory(getAndroidID())
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getAndroidID(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


    private fun uriToFile(context: Context, uri: Uri, fileNamePrefix: String): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let { stream ->
                val extension = getFileExtensionFromUri(context, uri)
                val tempFile = File.createTempFile(fileNamePrefix, ".$extension", context.cacheDir)
                tempFile.deleteOnExit()
                FileOutputStream(tempFile).use { outputStream ->
                    stream.copyTo(outputStream)
                }
                tempFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileExtensionFromUri(context: Context, uri: Uri): String? {
        var extension: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    val fileName = cursor.getString(nameIndex)
                    fileName.lastIndexOf('.').takeIf { it != -1 }?.let {
                        extension = fileName.substring(it + 1)
                    }
                }
            }
        }
        if (extension == null) {
            extension = context.contentResolver.getType(uri)?.substringAfterLast('/')
        }
        return extension ?: "tmp"
    }

}