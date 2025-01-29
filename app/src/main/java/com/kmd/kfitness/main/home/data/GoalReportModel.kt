package com.kmd.kfitness.main.home.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class GoalReportModel(
    @SerializedName("description")
    val description : String,
    @SerializedName("target_value")
    val target : Float,
    @SerializedName("end_date")
    val endDate : Date

) : Serializable
