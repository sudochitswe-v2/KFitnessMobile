package com.kmd.kfitness.general.helper

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MessageHelper (private val context: Context) {
    fun showPositiveDialog(
        title:String?,
        message: String?,
        positiveButtonText :String = "OK" ,
        onPositiveButtonClick: (() -> Unit)? = null
    ){
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositiveButtonClick?.invoke()
            dialog.dismiss()
        }
        builder.setCancelable(false) // Prevent dismissing the dialog by tapping outside
        builder.show()
    }
    fun showLongToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showShortToast(message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}