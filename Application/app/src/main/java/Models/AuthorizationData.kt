package Models

import java.util.*

class AuthorizationData {

    companion object{
        private lateinit var authorizationKey : String

        fun SetAuthorizationKey(username: String, password : String){
            val authenticator = username + ":" + password
            val encodedAuthenticatorKey: String = Base64.getEncoder().encodeToString(authenticator.toByteArray())
            authorizationKey = encodedAuthenticatorKey
        }

        fun GetAuthorizationKey() : String{
            return authorizationKey
        }
    }
}