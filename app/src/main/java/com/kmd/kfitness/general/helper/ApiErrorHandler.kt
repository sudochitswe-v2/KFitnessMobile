package com.kmd.kfitness.general.helper

import com.android.volley.VolleyError
import com.google.gson.Gson
import com.kmd.kfitness.general.error.ApiError
import java.nio.charset.Charset

class ApiErrorHandler (private val messageHelper: MessageHelper,private val gson : Gson) {

    fun onVolleyErrorShowSimpleDialog(error: VolleyError) {
        val errorMessage = when {
            error.networkResponse != null -> {
                // Handle network-related errors
                val statusCode = error.networkResponse.statusCode
                val errMsg = error.networkResponse.data?.toString(Charset.defaultCharset()) ?: "No error message"
                val data = try {
                    gson.fromJson(errMsg, ApiError::class.java)
                } catch (e: Exception) {
                    // Handle JSON parsing errors
                    ApiError("Failed to parse error response: ${e.message}")
                }
                "Error $statusCode: ${data.error}"
            }
            error.message != null -> {
                // Handle other errors (e.g., timeouts, no internet)
                "Error: ${error.message}"
            }
            else -> {
                // Handle unknown errors
                "An unknown error occurred"
            }
        }

        // Show the error message in a dialog
        messageHelper.showPositiveDialog("Error", errorMessage)
    }
}