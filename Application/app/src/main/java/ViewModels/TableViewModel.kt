package ViewModels

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

class TableViewModel(val tableView: TableFragment) : ViewModel()
{
    private lateinit var adapter : TaskItemAdapter
    private lateinit var getTaskList :  suspend (() -> MutableList<Task>)

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
        if (adapter != null)
        {
            adapter.onButtonClicked -= ::GetTaskDetails
        }

        if (tableView != null)
        {
            tableView.onGetAllReferences -= :: InitTableViewComponents
        }
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

            tableView.SetAdapter(adapter)
        }
    }

    private fun GetTaskDetails(task:Task)
    {
        val bundle = bundleOf("taskID" to task.id_t)
        Navigation.findNavController(tableView.requireView()).navigate(R.id.action_board_to_createTask, bundle)
        // TO DO: create a layout for task details and naviation from board fragment to it.
    }
}