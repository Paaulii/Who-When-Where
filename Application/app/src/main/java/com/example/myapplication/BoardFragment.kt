package com.example.myapplication

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class BoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.board, container, false)
        return view
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