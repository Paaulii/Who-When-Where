package com.example.myapplication

import Models.AuthorizationData
import Models.BoardRepository
import Models.Task
import ViewModels.BoardViewModel
import Views.BoardFragment
import android.provider.Settings.Global.getString
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.Assert.*
import java.net.HttpURLConnection
import java.net.URL

class RequestsTest
{
    @Test
    fun RequestsTasksTests()
    {
        SetAuthorizationKey()
        assertEquals(true, AreTasksValid(GetRequest("https://io.kamil.top:20420/tasks?status=To+do"), "To do"))
        assertEquals(true, AreTasksValid(GetRequest("https://io.kamil.top:20420/tasks?status=In+progress"), "In progress"))
        assertEquals(true, AreTasksValid(GetRequest("https://io.kamil.top:20420/tasks?status=Done"), "Done"))
    }

    @Test
    fun RequestsStressTest()
    {
        SetAuthorizationKey()
        for (i in 1..100)
        {
            assertEquals(true, IsValidJson(GetRequest("https://io.kamil.top:20420/tasks")))
        }
    }

    fun GetRequest(requestUrl: String) : String
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

        return tasks
    }

    fun IsValidJson(json: String) : Boolean
    {
        try
        {
            if (json.contains('['))
            {
                Json.decodeFromString<MutableList<Task>>(json)
            } else
            {
                mutableListOf(Json.decodeFromString<Task>(json))
            }
        }
        catch (e: Exception)
        {
            return false
        }

        return true
    }

    fun AreTasksValid(json: String, status: String) : Boolean
    {
        try
        {
            val tasksList = if (json.contains('['))
            {
                Json.decodeFromString<MutableList<Task>>(json)
            } else
            {
                mutableListOf(Json.decodeFromString<Task>(json))
            }

            for (task in tasksList)
            {
                if (task.status != status)
                {

                }
            }
        }
        catch (e: Exception)
        {
            return false
        }

        return true
    }

    fun SetAuthorizationKey()
    {
        AuthorizationData.SetAuthorizationKey("janek", "trudnehaslo")
    }
}