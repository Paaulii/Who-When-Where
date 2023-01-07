package Views

import ViewModels.ReportViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication.R
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

data class TasksReportData(var toDoCount: Int = 0,
                       var inProgressCount: Int = 0,
                       var doneCount: Int = 0,
                       var estimatedTimeSum: Float = 0.0f,
                       var realTimeSum: Float = 0.0f)
{}

class ReportFragment : Fragment() {
    private var reportViewModel = ReportViewModel(this)
    private var tasksReportData: TasksReportData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_report_to_board)
        }

        InitChart()
    }

    fun UpdateTasksReportData(newTasksReportData: TasksReportData)
    {
        tasksReportData = newTasksReportData
        InitChart()
    }

    private fun InitChart()
    {
        if (tasksReportData == null)
        {
            return
        }

        val todoValueTextView = view?.findViewById<TextView>(R.id.todo_Value)
        val inProgressValueTextView = view?.findViewById<TextView>(R.id.inprogress_Value)
        val doneValueTextView = view?.findViewById<TextView>(R.id.done_Value)
        val estimatedTimeValueTextView = view?.findViewById<TextView>(R.id.estimatedTime_Value)
        val realTimeValueTextView = view?.findViewById<TextView>(R.id.realTime_Value)
        val pieChart = view?.findViewById<PieChart>(R.id.piechart);

        todoValueTextView?.text = tasksReportData?.toDoCount.toString()
        inProgressValueTextView?.text = tasksReportData?.inProgressCount.toString()
        doneValueTextView?.text = tasksReportData?.doneCount.toString()
        estimatedTimeValueTextView?.text = tasksReportData?.estimatedTimeSum.toString()
        realTimeValueTextView?.text = tasksReportData?.realTimeSum.toString()

        pieChart?.addPieSlice(
            PieModel(
                R.string.toDo.toString(), todoValueTextView?.text.toString().toInt().toFloat(),
                Color.parseColor("#E1DD8E")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                R.string.inProgress.toString(), inProgressValueTextView?.text.toString().toInt().toFloat(),
                Color.parseColor("#4EAAC2")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                R.string.done.toString(), doneValueTextView?.text.toString().toInt().toFloat(),
                Color.parseColor("#85E682")
            )
        )

        pieChart?.startAnimation()
    }
}