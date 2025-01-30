package com.kmd.kfitness.main.workouts.data
import java.util.Date

class EditableWorkoutModel(
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
    endLongitude: String?
) : BaseWorkoutModel(
    id, userId, activityId, date, duration, distance, weight, repetitions, sets, status,
    startLatitude, startLongitude, endLatitude, endLongitude
)

