package Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import Utils.EventOneParam


class TaskItemAdapter (private val tasks: List<Task>) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    var onButtonClicked = EventOneParam<Task>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskCheckbox = itemView.findViewById<CheckBox>(R.id.taskCheckbox)
        val detailsBtn = itemView.findViewById<Button>(R.id.detailsBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val taskListView = inflater.inflate(R.layout.task_item,parent, false)
        return ViewHolder(taskListView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task : Task = tasks.get(position)
        val taskCheckboxHolder = holder.taskCheckbox

        holder.detailsBtn.setOnClickListener {
            onButtonClicked.invoke(task)
        }

        taskCheckboxHolder.setText(task.title)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
