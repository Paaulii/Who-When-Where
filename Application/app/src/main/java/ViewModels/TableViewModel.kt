package ViewModels

import Models.TableRepository
import Models.Task
import Models.TaskItemAdapter
import Views.TableFragment
import android.os.Looper
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TableViewModel(val tableView: TableFragment) : ViewModel()
{
    private lateinit var adapter : TaskItemAdapter
    private lateinit var getTaskList :  suspend (() -> MutableList<Task>)

    private var tableRepository = TableRepository()

    init
    {
        BindToEvents()
    }

    override fun onCleared()
    {
        super.onCleared()

        DetachFromEvents()
    }

    fun BindToEvents()
    {
        tableView.onGetAllReferences += :: InitTableViewComponents
        tableView.onInstanceCreated += :: HandleOnTableInstanceCreation
    }

    fun HandleOnTableInstanceCreation(getListMethod : suspend (() -> MutableList<Task>))
    {
        getTaskList = getListMethod
    }

    fun DetachFromEvents()
    {
        adapter.onButtonClicked -= ::GetTaskDetails
        tableView.onGetAllReferences -= :: InitTableViewComponents
    }

    fun InitTableViewComponents()
    {
        InitRecycleViewer()
    }

    private fun InitRecycleViewer()
    {
        viewModelScope.launch(Dispatchers.IO) {
            val taskList: MutableList<Task> = getTaskList.invoke()

            adapter = TaskItemAdapter(taskList)
            adapter.onButtonClicked += ::GetTaskDetails
            adapter.onChangeTaskState += ::ChangeTaskState
            tableView.SetAdapter(adapter)
        }
    }

    private fun GetTaskDetails(task:Task)
    {
        val bundle = bundleOf("taskID" to task.id_t)
        Navigation.findNavController(tableView.requireView()).navigate(R.id.action_board_to_createTask, bundle)
    }

    private fun ChangeTaskState(task: Task, newState: String, handlesEditTaskRequest: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            if (handlesEditTaskRequest) {
                val editedTask: Task = Task(
                    task.id_t,
                    task.title,
                    task.description,
                    task.category,
                    newState,
                    task.estimatedTime,
                    task.realTime,
                    task.id_u,
                    task.blockedBy
                )

                val jsonTask = Json.encodeToString(editedTask)
                tableRepository.EditTaskRequest(jsonTask)
                tableView.onContentChanged.invoke()
            }
        }
    }
}