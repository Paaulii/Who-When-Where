package Models

import DragListener
import Utils.EventOneParam
import Utils.EventTwoParam
import android.content.ClipData
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Math.abs
import java.lang.Thread.sleep
import kotlin.concurrent.thread


private const val DONE_STATUS = "Done"
class TaskItemAdapter (private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(), View.OnTouchListener {
    var onButtonClicked = EventOneParam<Task>()
    var onChangeTaskState = EventTwoParam<Task, String>()

    private var blockedTaskAlphaValue : Float = 0.4F
    private var taskRepository = TaskItemAdapterRepository()
    private lateinit var context : Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskText = itemView.findViewById<TextView>(R.id.taskText)
        val detailsBtn = itemView.findViewById<Button>(R.id.detailsBtn)
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_item)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
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

    fun GetTouchPositionFromDragEvent(item: View, event: MotionEvent): Point?
    {
        val rItem = Rect()
        item.getGlobalVisibleRect(rItem)
        return Point(rItem.left + Math.round(event.x), rItem.top + Math.round(event.y))
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
               thread {
                    val startTouchPos = GetTouchPositionFromDragEvent(v!!, event)
                    sleep(500)

                    val currentTouchPos = GetTouchPositionFromDragEvent(v!!, event)
                    if (abs(startTouchPos!!.x - currentTouchPos!!.x) <= 100.0 && abs(startTouchPos!!.y - currentTouchPos!!.y) <= 100.0)
                    {
                        val data = ClipData.newPlainText("", "")
                        val shadowBuilder = View.DragShadowBuilder(v)
                        v?.startDragAndDrop(data, shadowBuilder, v, 0)
                    }
                }
                return true
            }
        }
        return false
    }

    private fun SetDisabledTask(holder: ViewHolder, task: Task){
        if (task.blockedBy != null){
            runBlocking() {
                launch{
                    val dependentTask : Task? = taskRepository.GetTask(task.blockedBy!!)

                    if (dependentTask != null && dependentTask.status != DONE_STATUS)
                    {
                        holder.linearLayout.alpha = blockedTaskAlphaValue
                        holder.constraintLayout?.setOnTouchListener(null)
                    }
                }
            }
        }
    }
}

