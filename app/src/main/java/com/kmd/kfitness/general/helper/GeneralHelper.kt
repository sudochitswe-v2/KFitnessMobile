package com.kmd.kfitness.general.helper

import java.text.SimpleDateFormat
import java.util.Locale

object GeneralHelper {
     fun statusValues() : List<String> {
        return listOf("in_progress","completed");
    }
    val kDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)


}