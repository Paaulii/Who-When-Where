package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CreateTaskRepository
{
    val queryURLstring = "https://io.kamil.top:20420/addtask"

    suspend fun CreateTaskRequest(jsonBody: String)
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(queryURLstring)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json")

            con.outputStream.use { os ->
                val input = jsonBody.toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }

            con.inputStream.bufferedReader().use {
                try {
                    val ret = it.readText()
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }
        }
    }
}