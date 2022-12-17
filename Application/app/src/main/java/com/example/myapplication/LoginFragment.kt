package com.example.myapplication

import ViewModels.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {
    val loginViewModel = LoginViewModel(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{
            val username = view.findViewById<EditText>(R.id.username)
            val password = view.findViewById<EditText>(R.id.password)

            loginViewModel.TryToLogin(username?.text.toString(), password?.text.toString())
        }
    }

    fun DisplayLoginError()
    {
        Toast.makeText(context, "Invalid username or passowrd", Toast.LENGTH_LONG).show()
    }
}