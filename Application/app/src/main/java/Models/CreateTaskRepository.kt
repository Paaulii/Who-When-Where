package Models

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CreateTaskRepository
{
    val createTaskURLstring = "https://io.kamil.top:20420/addtask"
    val getUsersURLString = "https://io.kamil.top:20420/users"
    val getAllTaskURLString = "https://io.kamil.top:20420/tasks"
    val getAllExceptOneTaskURLString = "https://io.kamil.top:20420/tasks?drop="

    suspend fun CreateTaskRequest(jsonBody: String)
    {
        return withContext(Dispatchers.IO)
        {
            val url = URL(createTaskURLstring)
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

    suspend fun GetAllTaskExcept(taskID : Int) : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getAllExceptOneTaskURLString + taskID.toString())
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

    suspend fun GetAllTask() : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getAllTaskURLString)
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

    suspend fun GetUsers() : String{
        return withContext(Dispatchers.IO)
        {
            val url = URL(getUsersURLString)
            val con = url.openConnection() as HttpURLConnection

            con.setRequestProperty("Content-Type", "application/json")

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
            // TODO Do proper implementation
        }
    }

    suspend fun GetTask(taskID: Int) : Task
    {
        return withContext(Dispatchers.IO)
        {
            // TODO Implement proper getter task from server
            Task(-1, "Edited task", "Some other description", "Category-5", "Done", 5.0f, 1.0f, 2, null)
        }
    }
}