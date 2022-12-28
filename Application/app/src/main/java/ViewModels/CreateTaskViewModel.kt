package ViewModels

import Models.*
import Views.CreateTaskFragment
import android.R
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CreateTaskViewModel (var createTaskView : CreateTaskFragment) : ViewModel()
{
    private val createTaskRepository  = CreateTaskRepository()

    var users : MutableList<User> = mutableListOf()
    var tasks : MutableList<Task> = mutableListOf()
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
        SetupSpinners(userSpinner, tableTypeSpinner, taskSpinner)
    }

    fun SetupSpinners(userSpinner: Spinner, tableTypeSpinner: Spinner, taskSpinner : Spinner)
    {
        viewModelScope.launch(Dispatchers.IO){
            users = Json.decodeFromString<MutableList<User>>(createTaskRepository.GetUsers())
            Log.d("TASK ",createTaskRepository.GetUsers().toString())
            // Updating view must be called on a main thread
            val handler = android.os.Handler(Looper.getMainLooper())
            handler.post {
                val adapter = ArrayAdapter(
                    createTaskView.requireContext(),
                    R.layout.simple_spinner_item,
                    users
                )
                userSpinner.adapter = adapter
            }
        }

        viewModelScope.launch(Dispatchers.IO){

            val adapter = ArrayAdapter(createTaskView.requireContext(), R.layout.simple_spinner_item, tableTypes)
            tableTypeSpinner.adapter = adapter
        }

        viewModelScope.launch(Dispatchers.IO){

            val taskID = createTaskView.taskID

            if ( taskID == null || taskID != -1)
            {
                tasks = Json.decodeFromString<MutableList<Task>>(createTaskRepository.GetAllTask())
            }
            else
            {
                tasks = Json.decodeFromString<MutableList<Task>>(createTaskRepository.GetAllTaskExcept(taskID))
            }

            //tasks.add(Task("Task1","Desc","cat","To do",1.0f,2.0f,1, null))
            //tasks.add(Task("Task2","Desc1","cat1","In progress",1.0f,1.0f,1, null))

            val handler = android.os.Handler(Looper.getMainLooper())
            handler.post {
                val adapter = ArrayAdapter(
                    createTaskView.requireContext(),
                    R.layout.simple_spinner_item,
                    tasks
                )
                taskSpinner.adapter = adapter
            }

        }
    }
}