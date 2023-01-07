package Views

import DragListener
import ViewModels.BoardViewModel
import android.os.Bundle
import android.view.*
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class BoardFragment : Fragment() {
    private var boardViewModel = BoardViewModel(this);

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

        InitTables()
        view.findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_board_to_createTask)
        }

        val horizontalScrollView =
            view.findViewById(R.id.horizontal_scroll_view) as HorizontalScrollView

        DragListener.GetInstance()!!.SetScrollView(horizontalScrollView)
    }

    private fun InitTables(){
        childFragmentManager
            .beginTransaction()
            .add(R.id.toDoTableFragment, TableFragment.NewInstance("TO DO", boardViewModel::GetToDoList))
            .add(R.id.inProgressTableFragment, TableFragment.NewInstance("IN PROGRESS",boardViewModel::GetInProgressList))
            .add(R.id.doneTableFragment, TableFragment.NewInstance("DONE", boardViewModel::GetDoneList))
            .commit()
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