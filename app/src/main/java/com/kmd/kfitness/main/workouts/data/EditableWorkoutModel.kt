package com.kmd.kfitness.main.workouts.data

import java.io.Serializable
import com.google.gson.annotations.SerializedName
import java.util.Date

data class EditableWorkoutModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("activity_id")
    val activityId: Int,

    @SerializedName("date")
    val date: Date,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("distance")
    val distance: Float?,

    @SerializedName("weight")
    val weight: Float?,

    @SerializedName("repetitions")
    val repetitions: Int?,

    @SerializedName("sets")
    val sets: Int?,

    @SerializedName("status")
    val status: String,

    @SerializedName("start_latitude")
    val startLatitude: Double?,

    @SerializedName("start_longitude")
    val startLongitude: Double?,

    @SerializedName("end_latitude")
    val endLatitude: Double?,

    @SerializedName("end_longitude")
    val endLongitude: Double?
) : Serializable
