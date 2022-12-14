package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity() {
    private var toDoFragment = Table()
    private var inProgressFragment = Table()
    private var doneFragment = Table()
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        //ShowPopUpMenu(window.decorView.findViewById(android.R.id.content))
        //supportFragmentManager.beginTransaction().apply{
        //    replace(R.id.toDoTableFragment, toDoFragment)
        //    replace(R.id.inProgressTableFragment,inProgressFragment)
        //    replace(R.id.doneTableFragment, doneFragment)
        //    addToBackStack(null)
        //    commit()
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.popup_menu, menu)
        return true
    }
}