package com.kmd.kfitness.unauthorized.login.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public data class LoggedInUser(
    @SerializedName("claim")
    val claim: Claim,
    @SerializedName("cookie")
    val cookie: String
)

public data class Claim(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("weight")
    val weight: String
)