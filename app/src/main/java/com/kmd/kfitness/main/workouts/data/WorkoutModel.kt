package com.kmd.kfitness.main.workouts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

 class WorkoutModel(
    id: Int,
    userId: Int,
    activityId: Int,
    date: Date,
    duration: Int,
    distance: Float?,
    weight: Float?,
    repetitions: Int?,
    sets: Int?,
    status: String,
    startLatitude: String?,
    startLongitude: String?,
    endLatitude: String?,
    endLongitude: String?,

    @SerializedName("activity")
    val activity: String,

    @SerializedName("calories_per_minute")
    val caloriesPerMinute: Float

) : BaseWorkoutModel(
    id, userId, activityId, date, duration, distance, weight, repetitions, sets, status,
    startLatitude, startLongitude, endLatitude, endLongitude
) {
    fun toEditableModel() : EditableWorkoutModel{
        return EditableWorkoutModel(
            id,
            userId,
            activityId,
            date,
            duration,
            distance,
            weight,
            repetitions,
            sets,
            status,
            startLatitude,
            startLongitude,
            endLatitude,
            endLongitude
        )
    }
}

