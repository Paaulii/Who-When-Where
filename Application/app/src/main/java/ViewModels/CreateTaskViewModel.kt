package ViewModels

import Models.CreateTaskRepository
import Models.Task
import Models.TaskState
import Models.User
import Views.CreateTaskFragment
import android.R
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreateTaskViewModel (var createTaskView : CreateTaskFragment) : ViewModel()
{
    private val createTaskRepository  = CreateTaskRepository()

    val users : MutableList<User> = mutableListOf()
    val tasks : MutableList<Task> = mutableListOf()
    val tableTypes  = listOf("To do", "In progress", "Done")

    init
    {
        createTaskView.onGetAllReferences += ::HandleTaskCreationView
        createTaskView.onTaskCreate += ::CreateTask
        createTaskView.onTaskEdited += ::EditTask
    }

    fun TryGetTask(taskID: Int)
    {
        viewModelScope.launch {
            val task = createTaskRepository.GetTask(taskID)
            createTaskView.InitTaskInformation(task)
        }
    }

    override fun onCleared()
    {
        super.onCleared()
        if (createTaskView != null){
            createTaskView.onGetAllReferences -= ::HandleTaskCreationView
            createTaskView.onTaskCreate -= ::CreateTask
        }
    }

    private fun CreateTask(task: Task)
    {
        viewModelScope.launch {
            val jsonTask = Json.encodeToString(task)
            Log.d("TASK", jsonTask)
            createTaskRepository.CreateTaskRequest(jsonTask)
        }
    }

    private fun EditTask(task: Task)
    {
        viewModelScope.launch {
            val jsonTask = Json.encodeToString(task)
            Log.d("TASK", jsonTask)
            createTaskRepository.CreateTaskRequest(jsonTask)
        }
    }
    
    fun HandleTaskCreationView(userSpinner: Spinner, tableTypeSpinner: Spinner, taskSpinner : Spinner)
    {
        SetupSpinner(userSpinner, tableTypeSpinner, taskSpinner)
    }

    fun SetupSpinner(userSpinner: Spinner, tableTypeSpinner: Spinner, taskSpinner : Spinner)
    {
        viewModelScope.launch(Dispatchers.IO){
            // TO DO: create dedicated class for getting all users in system and returning that list to this place.

            users.add(User(0,"Paulina","Hałatek","phalatek","trudneHaslo1",123))
            users.add(User(1,"Kamil","Maciantowicz","kMaciantowicz","trudneHaslo2",345))
            users.add(User(2,"Paweł","Noras","pNoras","trudneHaslo3",564))


            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, users)
            userSpinner.adapter = adapter
        }

        viewModelScope.launch(Dispatchers.IO){

            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, tableTypes)
            tableTypeSpinner.adapter = adapter
        }

        viewModelScope.launch(Dispatchers.IO){

            tasks.add(Task("Task1","Desc","cat","To do",1.0f,2.0f,1))
            tasks.add(Task("Task2","Desc1","cat1","In progress",1.0f,1.0f,1))

            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, tasks)
            taskSpinner.adapter = adapter
        }
    }
}