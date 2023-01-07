package Views

import DragListener
import Models.Task
import Models.TaskItemAdapter
import Utils.EventOneParam
import Utils.EventZeroParam
import ViewModels.TableViewModel
import android.app.Activity
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

private const val ARG_HEADER = "Header"
private const val ARG_GET_LIST_FUNCTION = "GetListFunction"

class TableFragment : Fragment() {
    var onInstanceCreated = EventOneParam<suspend () -> MutableList<Task>>()
    var onGetAllReferences  = EventZeroParam()
    lateinit var recyclerView: RecyclerView

    private var tableViewModel = TableViewModel(this);
    private lateinit var noDataText : TextView
    private var header: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.headerText).text = header
        noDataText = view.findViewById(R.id.no_data_text)
        noDataText.setOnDragListener(DragListener.GetInstance())
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.tag = header!!.capitalize()
        onGetAllReferences.invoke()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            header = it.getString(ARG_HEADER)
        }
    }

    fun SetEmptyView(){
        val adapter = recyclerView.adapter as TaskItemAdapter
        val tasks = adapter.getList()

        if (tasks.isEmpty()){
            noDataText.visibility = VISIBLE
            recyclerView.visibility = GONE
        }
        else
        {
            noDataText.visibility = GONE
            recyclerView.visibility = VISIBLE
        }
    }
    fun SetAdapter(adapter: TaskItemAdapter)
    {
        (context as Activity).runOnUiThread {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            SetEmptyView()
        }
    }

    companion object {
        @JvmStatic
       fun NewInstance(header: String, listGetterMethod : suspend () -> MutableList<Task>) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HEADER, header)
                }

                onInstanceCreated.invoke(listGetterMethod)
            }
    }
}