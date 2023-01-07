package ViewModels

import Models.ReportRepository
import Models.Task
import Views.ReportFragment
import Views.TasksReportData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ReportViewModel(val reportView : ReportFragment): ViewModel()
{
    val reportRepository = ReportRepository()

    init
    {
        GetAndUpdateTasksData()
    }

    private fun GetAndUpdateTasksData()
    {
        viewModelScope.launch {
            var conditions = BoardViewModel.GetGroupByConditions()
            conditions = if (conditions.isNotEmpty()) '?' + conditions.substring(1) else conditions

            val tasks = Json.decodeFromString<MutableList<Task>>(reportRepository.GetTasks(conditions))

            val tasksReportData = TasksReportData()

            for (task in tasks)
            {
                tasksReportData.estimatedTimeSum += task.estimatedTime
                tasksReportData.realTimeSum += task.realTime

                when (task.status)
                {
                    "To do" -> tasksReportData.toDoCount++
                    "In progress" -> tasksReportData.inProgressCount++
                    "Done" -> tasksReportData.doneCount++
                }
            }

            reportView.UpdateTasksReportData(tasksReportData)
        }
    }
}