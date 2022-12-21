package ViewModels

import Models.State
import Models.Task
import Models.TaskItemAdapter
import Views.TableFragment

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager

class TableViewModel(val tableViewModel: TableFragment) : ViewModel()
{
    private lateinit var adapter : TaskItemAdapter

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
        tableViewModel.onGetAllReferences += :: InitTableViewComponents
    }

    fun DetachFromEvents()
    {
        if (adapter != null)
        {
            adapter.onButtonClicked -= ::GetTaskDetails
        }

        if (tableViewModel != null)
        {
            tableViewModel.onGetAllReferences -= :: InitTableViewComponents
        }
    }

    fun InitTableViewComponents()
    {
        InitRecycleViewer()
    }

    private fun InitRecycleViewer()
    {
        val taskList : MutableList<Task> = mutableListOf<Task>()
        taskList.add(Task(1,"Task 1","Description 1", "category1", State.ToDo,"1.5","2.0",1))

        adapter = TaskItemAdapter(taskList)
        adapter.onButtonClicked += ::GetTaskDetails

        tableViewModel.recyclerView.adapter = adapter
        tableViewModel.recyclerView.layoutManager = LinearLayoutManager(tableViewModel.requireContext())
    }

    private fun GetTaskDetails(task:Task)
    {
        // TO DO: create a layout for task details and naviation from board fragment to it.
    }
}