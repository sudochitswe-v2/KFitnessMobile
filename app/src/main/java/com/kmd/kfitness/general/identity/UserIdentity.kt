package com.kmd.kfitness.general.identity

import com.kmd.kfitness.unauthorized.login.data.model.LoggedInUser
import kotlin.math.log

//object UserIdentity {
//    private var id:Int? = null;
//    private var name:String? = null;
//    private var email:String? = null;
//    private var token:String? = null;
//
//
//    fun setUserIdentity(userId:Int,userName:String,userEmail:String,apiToken:String){
//        id = userId;
//        name = userName;
//        email = userEmail;
//        token = apiToken;
//    }
//}

class UserIdentity private constructor() {


    // Properties

    var id:Int? = null
        private set

    var name: String? = null
        private set

    var email: String? = null
        private set

    var accessToken: String? = null
        private set


    // Initialize method
    fun init(loggedInUser: LoggedInUser) {
        id = loggedInUser.claim.id
        name = loggedInUser.claim.name
        email = loggedInUser.claim.email
        accessToken = loggedInUser.cookie

    }
    fun destroy(){
        id = null
        name = null
        email = null
        accessToken = null
    }

    // Singleton instance
    companion object {
        val instance: UserIdentity by lazy { UserIdentity() }
    }
}

