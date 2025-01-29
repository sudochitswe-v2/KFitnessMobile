package com.kmd.kfitness.main.workouts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityModel(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("calories_per_minute")
    val caloriesPerMinute : Float

) : Serializable{
    override fun toString(): String {
        return name
    }
}
