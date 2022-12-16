package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class Board : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Toast.makeText(context, "View Created", Toast.LENGTH_SHORT).show()
        val view: View = inflater.inflate(R.layout.board, container, false)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Logout -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_board_to_login)
                true
            }
        else -> super.onOptionsItemSelected(item)
        }
    }
}