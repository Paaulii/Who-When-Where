package ViewModels

import Models.ReportFieldReferences
import Views.ReportFragment
import androidx.lifecycle.ViewModel

class ReportViewModel(val reportView : ReportFragment): ViewModel()
{
    init
    {
        reportView.onGetAllReferences += ::FillAllReportFields
    }

    override fun onCleared()
    {
        super.onCleared()
        reportView.onGetAllReferences -= :: FillAllReportFields
    }

    private fun FillAllReportFields(reportFieldReferences: ReportFieldReferences)
    {
        // TO DO: get from DB for a given user: all completed tasks,
        // all in progress tasks, all to do task, total estimated time, total real time
    }
}