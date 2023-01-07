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
        createTaskView.onEnterTaskEditMode += ::TryGetTask
    }

    override fun onCleared()
    {
        super.onCleared()

        createTaskView.onGetAllReferences -= ::HandleTaskCreationView
        createTaskView.onTaskCreate -= ::CreateTask
        createTaskView.onTaskEdited -= ::EditTask
        createTaskView.onEnterTaskEditMode -= ::TryGetTask
    }

    fun TryGetTask(taskID: Int)
    {
        viewModelScope.launch {
            val task : Task? = createTaskRepository.GetTask(taskID)

            if (task != null)
            {
                createTaskView.InitTaskInformation(task)
            }
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