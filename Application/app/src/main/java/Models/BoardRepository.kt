package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class BoardRepository
{
    val usersURLString = "https://io.kamil.top:20420/users"

    suspend fun GetTasks(requestUrl: String) : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(requestUrl)
            val con = url.openConnection() as HttpURLConnection
            var tasks = ""

            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.setRequestProperty("Authorization",authorizationKey);

            con.inputStream.bufferedReader().use {
                try {
                    tasks = it.readText()
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }

            tasks
        }
    }

    suspend fun GetAllUsers() : String
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(usersURLString)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")

            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.addRequestProperty("Authorization",authorizationKey);

            var usersJson = ""
            con.inputStream.bufferedReader().use {
                try {
                    usersJson = it.readText()
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }

            usersJson
        }
    }
}