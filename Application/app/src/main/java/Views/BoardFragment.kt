package Views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toDo: (() -> Unit) = {
            Log.d("TEST", "TO DO")
        }

        val inProg: (() -> Unit) = {
            Log.d("TEST", "TO DO")
        }

        val done: (() -> Unit) = {
            Log.d("TEST", "TO DO")
        }
        childFragmentManager
            .beginTransaction()
            .add(R.id.toDoTableFragment, TableFragment.NewInstance("TO DO",toDo))
            .add(R.id.inProgressTableFragment, TableFragment.NewInstance("IN PROGRESS",inProg))
            .add(R.id.doneTableFragment, TableFragment.NewInstance("DONE", done))
            .commit()

        view.findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_board_to_createTask)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.popup_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_Logout -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_board_to_login)
                true
            }
            R.id.menu_Report -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_board_to_report)
                true
            }
        else -> super.onOptionsItemSelected(item)
        }
    }
}