package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository
{
    val loginURLstring = "https://io.kamil.top:20420/foo"

    suspend fun MakeLoginRequest(jsonBody: String) : Boolean
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(loginURLstring)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json")

            con.outputStream.use { os ->
                val input = jsonBody.toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }

            var result = false

            con.inputStream.bufferedReader().use {
                try {
                    val ret = it.readText()
                    if (ret == "True\n")
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