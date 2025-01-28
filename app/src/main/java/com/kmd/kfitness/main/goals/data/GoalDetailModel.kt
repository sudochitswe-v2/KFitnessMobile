package com.kmd.kfitness.main.goals.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class GoalDetailModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("target_value")
    val target : Float,
    @SerializedName("start_date")
    val startDate : Date,
    @SerializedName("end_date")
    val endDate: Date,
    @SerializedName("status")
    val status: String,
    @SerializedName("progresses")
    val progresses : List<ProgressModel>
) : Serializable
