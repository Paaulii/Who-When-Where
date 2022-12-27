package ViewModels

import Models.TaskState
import Models.Task
import Views.BoardFragment
import androidx.lifecycle.ViewModel


class BoardViewModel (val boardView : BoardFragment) : ViewModel()
{
    fun GetToDoList() : MutableList<Task>
    {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task("Zadanie dla to do","Description 1", "category1", "To do",1.5f,2.0f,1))
        return taskList
    }

    fun GetInProgressList() : MutableList<Task>
    {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task("Zadanie dla in progress","Description 1", "category1", "In progress",1.5f,2.0f,1))
        return taskList
    }

    fun GetDoneList() : MutableList<Task>
    {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task("Zadanie dla done","Description 1", "category1", "Done",1.5f,2.0f,1))
        return taskList
    }
}