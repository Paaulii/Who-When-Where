package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class BoardRepository
{
    suspend fun GetTasks(requestUrl: String) : String{


        return withContext(Dispatchers.IO)
        {
            val url = URL(requestUrl)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")

            var tasks = ""
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
}