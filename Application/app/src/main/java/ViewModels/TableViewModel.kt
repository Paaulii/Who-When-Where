package ViewModels

import Models.Task
import Models.TaskItemAdapter
import Views.TableFragment

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager

class TableViewModel(val tableView: TableFragment) : ViewModel()
{
    private lateinit var adapter : TaskItemAdapter
    private lateinit var getTaskList :  (() -> MutableList<Task>)

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

    fun HandleOnTableInstanceCreation(getListMethod : (() -> MutableList<Task>))
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
        val taskList : MutableList<Task> = getTaskList.invoke()

        adapter = TaskItemAdapter(taskList)
        adapter.onButtonClicked += ::GetTaskDetails

        tableView.recyclerView.adapter = adapter
        tableView.recyclerView.layoutManager = LinearLayoutManager(tableView.requireContext())
    }

    private fun GetTaskDetails(task:Task)
    {
        // TO DO: create a layout for task details and naviation from board fragment to it.
    }
}