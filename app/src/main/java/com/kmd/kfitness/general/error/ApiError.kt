package com.kmd.kfitness.general.error

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("error")
    val error: String
)
