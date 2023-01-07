package Views


import Models.Task
import Models.TaskState
import Models.User
import Utils.EventOneParam
import Utils.EventThreeParam
import ViewModels.CreateTaskViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication.R


class CreateTaskFragment : Fragment() {
    var onTaskCreate = EventOneParam<Task>()
    var onTaskEdited = EventOneParam<Task>()
    var onGetAllReferences  = EventThreeParam<Spinner, Spinner, Spinner>()
    var onEnterTaskEditMode = EventOneParam<Int>()
    var taskID : Int? = null


    private lateinit var userSpinner : Spinner
    private lateinit var taskStateSpinner : Spinner
    private lateinit var titleText: EditText
    private lateinit var category: EditText
    private lateinit var description: EditText
    private lateinit var estimatedTime: EditText
    private lateinit var realTime: EditText
    private lateinit var dependencyCheckbox: CheckBox
    private lateinit var causerText : TextView
    private lateinit var tasksSpinner: Spinner

    private var createTaskViewModel  = CreateTaskViewModel(this)

    private var OnSaveButtonClickedFun = ::HandleTaskCreation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSpinner = view.findViewById(R.id.users_spinner)
        taskStateSpinner = view.findViewById(R.id.tableTypeSpinner)
        titleText = view.findViewById(R.id.title)
        category = view.findViewById(R.id.category)
        description = view.findViewById(R.id.description)
        estimatedTime = view.findViewById(R.id.estimated_time)
        realTime = view.findViewById(R.id.real_time)
        dependencyCheckbox = view.findViewById(R.id.dependencyCheckbox)
        causerText = view.findViewById(R.id.causerText)
        tasksSpinner = view.findViewById(R.id.tasks_spinner)

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_createTask_to_board)
        }

        view.findViewById<Button>(R.id.btnSaveTask).setOnClickListener{
            OnSaveButtonClickedFun()
        }

        dependencyCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                causerText.visibility = INVISIBLE
                tasksSpinner.visibility = INVISIBLE
            }
            else
            {
                causerText.visibility = VISIBLE
                tasksSpinner.visibility = VISIBLE
            }
        }

        dependencyCheckbox.isChecked = false;
        onGetAllReferences.invoke(userSpinner, taskStateSpinner, tasksSpinner)
    }

    fun PrepareEditTaskInformation(view: View){
        taskID = arguments?.getInt("taskID")
        val mainTitle = view.findViewById<TextView>(R.id.mainTitle)

        if (taskID != null && taskID != -1)
        {
            mainTitle.text = "Task Edition"
            OnSaveButtonClickedFun = ::HandleTaskEdit
            onEnterTaskEditMode.invoke(taskID!!)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun InitTaskInformation(task: Task)
    {
        titleText.setText(task.title)
        taskStateSpinner.setSelection(createTaskViewModel.tableTypes.indexOfFirst { task.status == it })
        userSpinner.setSelection(createTaskViewModel.users.indexOfFirst{ it.id_u == task.id_u })
        category.setText(task.category)
        description.setText(task.description)
        estimatedTime.setText(task.estimatedTime.toString())
        realTime.setText(task.realTime.toString())

        val hasDependency : Boolean = task.blockedBy != null
        dependencyCheckbox.isChecked = hasDependency;

        if (hasDependency) {
            val position: Int = GetTaskPositionInSpinner(task.blockedBy!!)

            if (position != -1) {
                tasksSpinner.setSelection(position)
            }
        }
    }

    private fun GetTaskPositionInSpinner(taskID : Int) : Int
    {
        val adapter : Adapter = tasksSpinner.adapter;
        for (i in 1 until  adapter.getCount())
        {
            val task: Task = adapter.getItem(i) as Task
            if (task.id_t == taskID)
            {
                return i
            }
        }

        return -1
    }

    private fun HandleTaskCreation()
    {
        if (AreAllFieldsFilled() && CheckNumberFields())
        {
            val newTask = GenerateTaskObject()
            onTaskCreate.invoke(newTask)

            Navigation.findNavController(requireView()).navigate(R.id.action_createTask_to_board)
        }
        else
        {
            Toast.makeText(context,"Please, fill all fields.",Toast.LENGTH_SHORT).show();
        }

    }

    private fun HandleTaskEdit()
    {
        if (AreAllFieldsFilled() && CheckNumberFields())
        {
            val taskID = arguments?.getInt("taskID")!!

            val newTask = GenerateTaskObject()
            newTask.id_t = taskID

            onTaskEdited.invoke(newTask)

            Navigation.findNavController(requireView()).navigate(R.id.action_createTask_to_board)
        }
        else
        {
            Toast.makeText(context,"Please, fill all fields.",Toast.LENGTH_SHORT).show();
        }
    }

    private fun GenerateTaskObject() : Task
    {
        val user : User  =  userSpinner.selectedItem as User
        val isTaskDependent = dependencyCheckbox.isChecked()

        var blockedBy : Int? = null

        if (isTaskDependent){
            val blockingTask : Task = tasksSpinner.selectedItem as Task
            blockedBy = blockingTask.id_t
        }

        return Task(
            -1,
                titleText.text.toString(),
                description.text.toString(),
                category.text.toString(),
                taskStateSpinner.selectedItem.toString(),
                estimatedTime.text.toString().toFloat(),
                realTime.text.toString().toFloat(),
                user.id_u,
                blockedBy)
    }

    private fun AreAllFieldsFilled() : Boolean
    {
        if (userSpinner.selectedItem.toString().isNotEmpty() &&
            taskStateSpinner.selectedItem.toString().isNotEmpty() &&
            titleText.text.isNotEmpty() && category.text.isNotEmpty() &&
            description.text.isNotEmpty() && estimatedTime.text.isNotEmpty() &&
            realTime.text.isNotEmpty()
           )
        {
            return true
        }
        return false
    }

    private fun CheckNumberFields() : Boolean {
        if (estimatedTime.text.toString().toFloatOrNull() != null &&
                realTime.text.toString().toFloatOrNull() != null)
        {
            return true
        }

        return false
    }
}