package Models

import DragListener
import Utils.EventOneParam
import Utils.EventThreeParam
import Utils.EventTwoParam
import android.content.ClipData
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class TaskItemAdapter (private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(), View.OnTouchListener {

    private var blockedTaskAlphaValue : Float = 0.4F
    var onButtonClicked = EventOneParam<Task>()
    var onChangeTaskState = EventThreeParam<Task, String, Boolean>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskText = itemView.findViewById<TextView>(R.id.taskText)
        val detailsBtn = itemView.findViewById<Button>(R.id.detailsBtn)
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_item)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val taskListView = inflater.inflate(R.layout.task_item,parent, false)
        return ViewHolder(taskListView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task : Task = tasks.get(position)
        val taskCheckboxHolder = holder.taskText
        holder.constraintLayout?.tag = position
        holder.constraintLayout?.setOnTouchListener(this)
        holder.constraintLayout?.setOnDragListener(DragListener.GetInstance())

        SetDisabledTask(holder, task)

        holder.detailsBtn.setOnClickListener {
            onButtonClicked.invoke(task)
        }

        taskCheckboxHolder.setText(task.title)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateList(list: MutableList<Task>) {
        this.tasks = list
    }

    fun getList(): MutableList<Task> = this.tasks


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                thread {
                    sleep(500)
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(v)
                    v?.startDragAndDrop(data, shadowBuilder, v, 0)
                }
                return true
            }
        }
        return false
    }

    private fun SetDisabledTask(holder: ViewHolder, task: Task){
        if (task.blockedBy != null){
            holder.linearLayout.alpha = blockedTaskAlphaValue
            holder.constraintLayout?.setOnTouchListener(null)
        }
    }
}

