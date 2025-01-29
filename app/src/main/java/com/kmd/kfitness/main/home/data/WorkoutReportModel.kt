package com.kmd.kfitness.main.home.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class WorkoutReportModel(
    @SerializedName("activity")
    val activity : String,
    @SerializedName("date")
    val date : Date,
    @SerializedName("status")
    val status: String,
    @SerializedName("duration")
    val duration : Int
) : Serializable
