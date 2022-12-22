package ViewModels

import Models.State
import Models.Task
import Views.BoardFragment
import Views.TableFragment
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel


class BoardViewModel (val boardView : BoardFragment) : ViewModel()
{
    val getToDoList: (() -> MutableList<Task>) = {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task(1,"Zadanie dla to do","Description 1", "category1", State.ToDo,"1.5","2.0",1))
        taskList
    }

    val getInProgressList: (() -> MutableList<Task>) = {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task(1,"Zadanie dla in progress","Description 1", "category1", State.ToDo,"1.5","2.0",1))
        taskList
    }

    val getDoneList: (() -> MutableList<Task>) = {
        val taskList : MutableList<Task> = mutableListOf()
        taskList.add(Task(1,"Zadanie dla done","Description 1", "category1", State.ToDo,"1.5","2.0",1))
        taskList
    }


}