package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ReportRepository {

    val baseURL = "https://io.kamil.top:20420/tasks"

    suspend fun GetTasks(conditions: String) : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(baseURL + conditions)
            val con = url.openConnection() as HttpURLConnection
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