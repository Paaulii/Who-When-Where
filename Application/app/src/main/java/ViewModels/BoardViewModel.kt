package ViewModels

import Models.BoardRepository
import Models.Task
import Models.User

import Views.BoardFragment
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.myapplication.R
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

enum class GroupBy
{
    USER, CATEGORY
}

class BoardViewModel (val boardView : BoardFragment) : ViewModel()
{
    private val boardRepository = BoardRepository()

    private var users = mutableListOf<User>()
    private var categories = mutableListOf<String>()

    companion object
    {
        var groupByUser: User? = null
        var groupByCategory: String? = null
    }

    fun BindToDelegates()
    {
        boardView.OnMenuItemSelected += ::HandleMenuItemSelected
    }

    suspend fun GetToDoList() : MutableList<Task>
    {

        return GetTaskList(boardView.resources.getString(R.string.getAllToDoTasks) + GetGroupByConditions())
    }

    suspend fun GetInProgressList() : MutableList<Task>
    {
        return GetTaskList(boardView.resources.getString(R.string.getAllInProgressTasks) + GetGroupByConditions())
    }

    suspend fun GetDoneList() : MutableList<Task>
    {
        return GetTaskList(boardView.resources.getString(R.string.getAllDoneTasks) + GetGroupByConditions())
    }

    fun GetGroupByConditions() : String
    {
        val userCondition = if (groupByUser != null) "&id_u=" + groupByUser?.id_u.toString() else ""
        val categoryCondition = if (groupByCategory != null) "&category=$groupByCategory" else ""
        return userCondition + categoryCondition
    }

    suspend fun GetTaskList(url: String) : MutableList<Task>
    {
        val taskList : MutableList<Task>
        val json = boardRepository.GetTasks(url)
        taskList = if (json.contains('['))
        {
            Json.decodeFromString<MutableList<Task>>(json)
        } else
        {
            mutableListOf(Json.decodeFromString<Task>(json))
        }

        return taskList
    }

    private fun HandleMenuItemSelected(itemID: Int)
    {
        when (itemID) {
            R.id.menu_Logout -> Navigation.findNavController(boardView.requireView()).navigate(R.id.action_board_to_login)
            R.id.menu_Report -> Navigation.findNavController(boardView.requireView()).navigate(R.id.action_board_to_report)
            R.id.menu_ReloadTasks -> boardView.UpdateTables()
            R.id.menu_GroupByUser -> HandleGroupByClicked(GroupBy.USER)
            R.id.menu_GroupByCategory -> HandleGroupByClicked(GroupBy.CATEGORY)
            R.id.menu_ResetGrouping -> {
                groupByUser = null
                groupByCategory = null
                boardView.UpdateTables()
            }
        }
    }

    private fun HandleGroupByClicked(groupByValue: GroupBy)
    {
        viewModelScope.launch {
            val builder = AlertDialog.Builder(boardView.context)
            val title = when (groupByValue)
            {
                GroupBy.USER -> "Group By User"
                GroupBy.CATEGORY -> "Group By Category"
            }
            builder.setTitle(title)

            val groupByList = when (groupByValue)
            {
                GroupBy.USER -> {
                    UpdateUsers()
                    users
                }
                GroupBy.CATEGORY -> {
                    UpdateCategories()
                    categories
                }
            }

            val viewInflated = LayoutInflater.from(boardView.context).inflate(R.layout.grouping_layout, boardView.view as ViewGroup, false)

            val sp = viewInflated.findViewById<Spinner>(R.id.group_Spinner)
            sp.adapter = ArrayAdapter(
                boardView.requireContext(),
                android.R.layout.simple_spinner_item,
                groupByList
            )
            builder.setView(viewInflated)

            builder.setPositiveButton(
                "OK",
            ) { dialog, _ ->
                dialog.dismiss()
                when (groupByValue)
                {
                    GroupBy.USER -> groupByUser = sp.selectedItem as User
                    GroupBy.CATEGORY -> groupByCategory = sp.selectedItem as String
                }
                boardView.UpdateTables()
            }

            builder.setNegativeButton(
                "CANCEL",
            ) { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }
    }

    suspend fun UpdateUsers()
    {
        users = Json.decodeFromString(boardRepository.GetAllUsers())
    }

    suspend fun UpdateCategories()
    {
        val tasks = Json.decodeFromString<MutableList<Task>>(boardRepository.GetTasks("https://io.kamil.top:20420/tasks"))
        categories.clear()

        for (task in tasks)
        {
            if (!categories.contains(task.category))
            {
                categories.add(task.category)
            }
        }
    }
}