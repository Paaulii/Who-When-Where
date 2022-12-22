package Views

import Models.Task
import Utils.EventOneParam
import Utils.EventZeroParam
import ViewModels.BoardViewModel
import ViewModels.TableViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

private const val ARG_HEADER = "Header"
private const val ARG_GET_LIST_FUNCTION = "GetListFunction"

class TableFragment : Fragment() {
    var onInstanceCreated = EventOneParam<() -> MutableList<Task>>()
    var onGetAllReferences  = EventZeroParam()
    lateinit var recyclerView: RecyclerView

    private var tableViewModel = TableViewModel(this);
    private var header: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.headerText).text = header
        recyclerView = view.findViewById(R.id.recyclerView)
        onGetAllReferences.invoke()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            header = it.getString(ARG_HEADER)
        }
    }

    companion object {
        @JvmStatic
       fun NewInstance(header: String, listGetterMethod : () -> MutableList<Task>) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HEADER, header)
                }

                onInstanceCreated.invoke(listGetterMethod)
            }
    }
}