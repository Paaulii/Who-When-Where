package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class CreateTaskRepository
{
    val createTaskURLstring = "https://io.kamil.top:20420/addtask"
    val editTaskURLString = "https://io.kamil.top:20420/updatetask"
    val getUsersURLString = "https://io.kamil.top:20420/users"
    val getAllTaskURLString = "https://io.kamil.top:20420/tasks"
    val getAllExceptOneTaskURLString = "https://io.kamil.top:20420/tasks?drop="
    val getTaskURLString = "https://io.kamil.top:20420/tasks?id="
    val deleteTaskURLString = "https://io.kamil.top:20420/deletetask"

    suspend fun CreateTaskRequest(jsonBody: String)
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(createTaskURLstring)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.addRequestProperty("Authorization",authorizationKey);

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

    suspend fun GetAllTaskExcept(taskID : Int) : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getAllExceptOneTaskURLString + taskID.toString())
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.setRequestProperty("Authorization",authorizationKey);

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

    suspend fun GetAllTask() : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getAllTaskURLString)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.addRequestProperty("Authorization",authorizationKey);

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

    suspend fun GetUsers() : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getUsersURLString)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.setRequestProperty("Authorization",authorizationKey);

            var users = ""
            con.inputStream.bufferedReader().use {
                try {
                    users = it.readText()
                } catch (e: Exception)
                {
                    Log.d("NETWORK-ERROR", e.message!!)
                }
            }

            users
        }
    }

    suspend fun EditTaskRequest(jsonBody: String)
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(editTaskURLString)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"

            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.addRequestProperty("Authorization",authorizationKey);

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

    suspend fun GetTask(taskID: Int) : Task?
    {
            return withContext(Dispatchers.IO)
            {
                val url = URL(getTaskURLString + taskID)
                val con = url.openConnection() as HttpURLConnection

                con.setRequestProperty("Content-Type", "application/json")
                val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
                con.addRequestProperty("Authorization",authorizationKey);

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

    suspend fun DeleteTask(jsonBody: String)
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(deleteTaskURLString)
            val con = url.openConnection() as HttpURLConnection

            con.doOutput = true
            con.requestMethod = "POST"

            con.setRequestProperty("Content-Type", "application/json")
            val authorizationKey: String = AuthorizationData.GetAuthorizationKey()
            con.addRequestProperty("Authorization",authorizationKey);

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