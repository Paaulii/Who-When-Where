package Views


import Models.Task
import Models.TaskState
import Models.User
import Utils.EventOneParam
import Utils.EventTwoParam
import ViewModels.CreateTaskViewModel
import android.opengl.Visibility
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
    var onUserCreate = EventOneParam<Task>()
    var onGetAllReferences  = EventTwoParam<Spinner, Spinner>()
    private lateinit var userSpinner : Spinner
    private lateinit var tableTypeSpinner : Spinner
    private lateinit var titleText: EditText
    private lateinit var category: EditText
    private lateinit var description: EditText
    private lateinit var estimatedTime: EditText
    private lateinit var realTime: EditText
    private lateinit var dependencyCheckbox: CheckBox
    private lateinit var causerText : TextView
    private lateinit var tasksSpinner: Spinner

    private lateinit var taskState: TaskState

    private var createTaskViewModel  = CreateTaskViewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSpinner = view.findViewById(R.id.users_spinner)
        tableTypeSpinner = view.findViewById(R.id.tableTypeSpinner)
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
            HandleTaskCreation()
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

        dependencyCheckbox.setChecked(false);
        onGetAllReferences.invoke(userSpinner, tableTypeSpinner)
    }

    private fun HandleTaskCreation()
    {
        if (AreAllFieldsFilled())
        {
            val user : User  =  userSpinner.selectedItem as User

            var newTask : Task = Task(
                0,
                titleText.text.toString(),
                description.text.toString(),
                category.text.toString(),
                TaskState.values()[tableTypeSpinner.getSelectedItemPosition()],
                estimatedTime.text.toString(),
                realTime.text.toString(),
                user.id_u
            )

            onUserCreate.invoke(newTask)
            Navigation.findNavController(requireView()).navigate(R.id.action_createTask_to_board)
        }
        else
        {
            Toast.makeText(context,"Please, fill all fields.",Toast.LENGTH_SHORT).show();
        }

    }

    fun AreAllFieldsFilled() : Boolean
    {
        if (userSpinner.selectedItem.toString().isNotEmpty() &&
            tableTypeSpinner.selectedItem.toString().isNotEmpty() &&
            titleText.text.isNotEmpty() && category.text.isNotEmpty() &&
            description.text.isNotEmpty() && estimatedTime.text.isNotEmpty() &&
            realTime.text.isNotEmpty()
           )
        {
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}