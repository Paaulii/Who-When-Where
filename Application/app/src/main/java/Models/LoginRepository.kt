package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository
{
    val loginURLstring = "https://io.kamil.top:20420/login"

    suspend fun MakeLoginRequest() : Boolean
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(loginURLstring)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"

            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.setRequestProperty("Authorization",authorizationKey);

            var result = false

            con.inputStream.bufferedReader().use {
                try {
                    val ret = it.readText()
                    if (ret == "True")
                    {
                        result = true
                    }
                    else {}
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }

            result
        }
    }
}