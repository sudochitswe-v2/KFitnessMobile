package com.kmd.kfitness.main.goals.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ProgressModel(
    @SerializedName("id")
    val id :Int,
    @SerializedName("goal_id")
    val goalId :Int,
    @SerializedName("date")
    val date: Date,
    @SerializedName("current_value")
    val currentValue: Float,
    @SerializedName("notes")
    val notes: String
) : Serializable
