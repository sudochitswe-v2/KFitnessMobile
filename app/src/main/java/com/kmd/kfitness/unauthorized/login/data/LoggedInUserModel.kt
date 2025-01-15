package com.kmd.kfitness.unauthorized.login.data

import com.google.gson.annotations.SerializedName
/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
 data class LoggedInUserModel(
    @SerializedName("claim")
    val claim: ClaimModel,
    @SerializedName("cookie")
    val cookie: String
)
