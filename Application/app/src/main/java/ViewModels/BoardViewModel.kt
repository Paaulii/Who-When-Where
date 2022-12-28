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

        val url : String =  boardView.resources.getString(R.string.getAllToDoTasks)
        val taskList : MutableList<Task>  = Json.decodeFromString<MutableList<Task>>(boardRepository.GetTasks(url))

        return taskList
    }

    suspend fun GetInProgressList() : MutableList<Task>
    {
        val url : String =  boardView.resources.getString(R.string.getAllInProgressTasks)
        val taskList : MutableList<Task>  = Json.decodeFromString<MutableList<Task>>(boardRepository.GetTasks(url))

        return taskList
    }

    suspend fun GetDoneList() : MutableList<Task>
    {
        val url : String =  boardView.resources.getString(R.string.getAllDoneTasks)
        val taskList : MutableList<Task>  = Json.decodeFromString<MutableList<Task>>(boardRepository.GetTasks(url))

        return taskList
    }
}