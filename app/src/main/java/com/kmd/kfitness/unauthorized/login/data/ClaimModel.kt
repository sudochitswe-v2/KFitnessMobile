package com.kmd.kfitness.unauthorized.login.data

import com.google.gson.annotations.SerializedName

data class ClaimModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
