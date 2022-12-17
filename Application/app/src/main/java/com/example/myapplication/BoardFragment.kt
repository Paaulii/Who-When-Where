package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
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

        childFragmentManager
            .beginTransaction()
            .add(R.id.toDoTableFragment, TableFragment.NewInstance("TO DO"))
            .add(R.id.inProgressTableFragment, TableFragment.NewInstance("IN PROGRESS"))
            .add(R.id.doneTableFragment, TableFragment.NewInstance("DONE"))
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