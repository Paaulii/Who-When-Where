package ViewModels

import Models.BoardRepository
import Models.Task

import Views.BoardFragment

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class BoardViewModel (val boardView : BoardFragment) : ViewModel()
{
    private val boardRepository = BoardRepository()

    suspend fun GetToDoList() : MutableList<Task>
    {
        return GetTaskList(boardView.resources.getString(R.string.getAllToDoTasks))
    }

    suspend fun GetInProgressList() : MutableList<Task>
    {
        return GetTaskList(boardView.resources.getString(R.string.getAllInProgressTasks))
    }

    suspend fun GetDoneList() : MutableList<Task>
    {
        return GetTaskList(boardView.resources.getString(R.string.getAllDoneTasks))
    }

    suspend fun GetTaskList(url: String) : MutableList<Task>
    {
        val taskList : MutableList<Task>
        val json = boardRepository.GetTasks(url)
        taskList = if (json.contains('['))
        {
            Json.decodeFromString<MutableList<Task>>(json)
        } else
        {
            mutableListOf(Json.decodeFromString<Task>(json))
        }

        return taskList
    }
}