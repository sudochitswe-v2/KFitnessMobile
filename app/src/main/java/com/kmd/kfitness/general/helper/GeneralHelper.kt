package com.kmd.kfitness.general.helper

import java.text.SimpleDateFormat
import java.util.Locale

object GeneralHelper {
     fun statusValues() : List<String> {
        return listOf("in_progress","completed");
    }
    const val K_DATE_FORMAT = "yyyy-MM-dd"
    val kDateFormat = SimpleDateFormat(K_DATE_FORMAT, Locale.US)


}