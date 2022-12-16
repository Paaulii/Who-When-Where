package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class Login : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{
            TryToLogin();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun TryToLogin()
    {
        if (false)
        {
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_board)
        }
        else
        {
            Toast.makeText(context, "Invalid username or passowrd", Toast.LENGTH_LONG).show()
        }
    }
}