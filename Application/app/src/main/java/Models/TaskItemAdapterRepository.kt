package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class TaskItemAdapterRepository {
    val getTaskURLString = "https://io.kamil.top:20420/tasks?id="

    suspend fun GetTask(taskID: Int) : Task?
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(getTaskURLString + taskID)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")

            var task : Task? = null

            con.inputStream.bufferedReader().use {
                try {
                    val ret = it.readText()
                    task = Json.decodeFromString<Task>(ret)
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }

            task
        }
    }
}