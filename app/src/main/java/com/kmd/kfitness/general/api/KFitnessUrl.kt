package com.kmd.kfitness.general.api

 object KFitnessUrl {
      private const val BASE_URL = "http://10.0.2.2:1080/kfitness/api"
     const val LOGIN = "$BASE_URL/auth/login.php"
     const val REGISTER = "$BASE_URL/auth/register.php"
     const val ACTIVITIES = "$BASE_URL/activities/index.php"
     const val GOALS = "$BASE_URL/goals/index.php"
     const val WORKOUTS = "$BASE_URL/workouts/index.php"
     const val PROGRESSES = "$BASE_URL/goals/progresses/index.php"
     const val REPORT = "$BASE_URL/report/index.php"
}