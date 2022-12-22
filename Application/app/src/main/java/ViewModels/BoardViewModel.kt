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
        taskList.add(Task(1,"Zadanie dla to do","Description 1", "category1", TaskState.ToDo,"1.5","2.0",1))
        return taskList
    }

    fun GetInProgressList() : MutableList<Task>
    {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task(1,"Zadanie dla in progress","Description 1", "category1", TaskState.ToDo,"1.5","2.0",1))
        return taskList
    }

    fun GetDoneList() : MutableList<Task>
    {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task(1,"Zadanie dla done","Description 1", "category1", TaskState.ToDo,"1.5","2.0",1))
        return taskList
    }
}