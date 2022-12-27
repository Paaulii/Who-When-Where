package Models

import android.widget.TextView

data class ReportFieldReferences(
    var completedTask : TextView,
    var inProgressTask: TextView,
    var toDoTask: TextView,
    var totalEstimatedTime : TextView,
    var totalRealTime: TextView
)
