package com.kmd.kfitness.unauthorized.register.data

data class UserRegisterModel(
    val name:String,
    val email:String,
    val password:String,
    val height : Double,
    val weight: Double,
)
