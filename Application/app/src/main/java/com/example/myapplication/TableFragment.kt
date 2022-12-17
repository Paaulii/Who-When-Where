package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_HEADER = "Header"

class TableFragment : Fragment() {

    private var header: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.headerText).text = header

        view.findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_board_to_createTask)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            header = it.getString(ARG_HEADER)
        }
    }

    companion object {
        @JvmStatic
        fun NewInstance(header: String) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HEADER, header)
                }
            }
    }
}