package ViewModels

import Models.Task
import Models.TaskState
import Models.User
import Views.CreateTaskFragment
import android.R
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateTaskViewModel (var createTaskView : CreateTaskFragment) : ViewModel()
{

    init
    {
        createTaskView.onGetAllReferences += ::HandleTaskCreationView
        createTaskView.onTaskCreate += ::CreateTask
    }

    override fun onCleared()
    {
        super.onCleared()
        if (createTaskView != null){
            createTaskView.onGetAllReferences -= ::HandleTaskCreationView
            createTaskView.onTaskCreate -= ::CreateTask
        }
    }

    fun CreateTask(task: Task)
    {
        // TO DO: send new task to server - mysql insert must be without task id, it should auto increment that value in database.
    }

    
    fun HandleTaskCreationView(userSpinner: Spinner, tableTypeSpinner: Spinner, taskSpinner : Spinner)
    {
        SetupSpinner(userSpinner, tableTypeSpinner, taskSpinner)
    }

    fun SetupSpinner(userSpinner: Spinner, tableTypeSpinner: Spinner, taskSpinner : Spinner)
    {
        viewModelScope.launch(Dispatchers.IO){
            // TO DO: create dedicated class for getting all users in system and returning that list to this place.
            val users : MutableList<User> = mutableListOf()

            users.add(User(0,"Paulina","Hałatek","phalatek","trudneHaslo1",123))
            users.add(User(1,"Kamil","Maciantowicz","kMaciantowicz","trudneHaslo2",345))
            users.add(User(2,"Paweł","Noras","pNoras","trudneHaslo3",564))


            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, users)
            userSpinner.adapter = adapter
        }

        viewModelScope.launch(Dispatchers.IO){
            val tableTypes  = listOf("To Do", "In Progress", "Done")

            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, tableTypes)
            tableTypeSpinner.adapter = adapter
        }

        viewModelScope.launch(Dispatchers.IO){
            val tasks : MutableList<Task> = mutableListOf()

            tasks.add(Task(0,"Task1","Desc","cat",TaskState.ToDo,"1","2",1))
            tasks.add(Task(1,"Task2","Desc1","cat1",TaskState.ToDo,"1","2",1))

            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, tasks)
            taskSpinner.adapter = adapter
        }
    }
}