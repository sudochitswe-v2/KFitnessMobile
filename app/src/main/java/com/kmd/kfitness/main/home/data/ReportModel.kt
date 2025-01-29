package com.kmd.kfitness.main.home.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReportModel(
    @SerializedName("workouts")
    val workouts: List<WorkoutReportModel> ,
    @SerializedName("goals")
    val goals : List<GoalReportModel>,
    @SerializedName("calories")
    val calories : Float
) : Serializable