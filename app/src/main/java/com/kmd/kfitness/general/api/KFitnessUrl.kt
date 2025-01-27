package com.kmd.kfitness.general.api

 object KFitnessUrl {
      private const val BASE_URL = "http://10.0.2.2:1080/kfitness/api"
     const val LOGIN = "$BASE_URL/auth/login.php"
     const val REGISTER = "$BASE_URL/auth/register.php"
     const val GOALS = "$BASE_URL/goals/index.php"
}