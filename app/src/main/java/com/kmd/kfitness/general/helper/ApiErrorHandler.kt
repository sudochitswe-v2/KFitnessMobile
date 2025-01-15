package com.kmd.kfitness.general.helper

import com.android.volley.VolleyError
import com.google.gson.Gson
import com.kmd.kfitness.general.error.ApiError
import java.nio.charset.Charset

class ApiErrorHandler (private val messageHelper: MessageHelper,private val gson : Gson) {

    fun onVolleyErrorShowSimpleDialog(error : VolleyError){
        val errMsg = error.networkResponse.data.toString(Charset.defaultCharset())
        val data =  gson.fromJson(errMsg, ApiError::class.java)
        messageHelper.showPositiveDialog("Error",data.error)
    }
}