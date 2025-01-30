package com.kmd.kfitness.main.workouts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

open class BaseWorkoutModel(
    @SerializedName("id")
    open val id: Int,

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
    val startLatitude: String?,

    @SerializedName("start_longitude")
    val startLongitude: String?,

    @SerializedName("end_latitude")
    val endLatitude: String?,

    @SerializedName("end_longitude")
    val endLongitude: String?
) : Serializable

