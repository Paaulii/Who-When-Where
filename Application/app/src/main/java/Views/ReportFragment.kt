package Views

import Models.ReportFieldReferences
import Utils.EventOneParam
import ViewModels.ReportViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication.R

class ReportFragment : Fragment() {
    val onGetAllReferences = EventOneParam<ReportFieldReferences>()

    private lateinit var completedTask : TextView
    private lateinit var inProgressTask: TextView
    private lateinit var toDoTask: TextView
    private lateinit var totalEstimatedTime : TextView
    private lateinit var totalRealTime: TextView

    private var reportViewModel = ReportViewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        completedTask = view.findViewById<TextView>(R.id.amount_of_completed_tasks)
        inProgressTask = view.findViewById<TextView>(R.id.amount_of_in_progress_tasks)
        toDoTask = view.findViewById<TextView>(R.id.amount_of_toDo_tasks)
        totalEstimatedTime = view.findViewById<TextView>(R.id.estimated_time)
        totalRealTime = view.findViewById<TextView>(R.id.real_time)

        val reportFieldReferences : ReportFieldReferences = ReportFieldReferences(completedTask,
                                                inProgressTask,
                                                toDoTask,
                                                totalEstimatedTime,
                                                totalRealTime)

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_report_to_board)
        }

        onGetAllReferences.invoke(reportFieldReferences)
    }
}