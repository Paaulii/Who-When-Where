package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity() {
    private var toDoFragment = Table()
    private var inProgressFragment = Table()
    private var doneFragment = Table()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board)
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.toDoTableFragment, toDoFragment)
            replace(R.id.inProgressTableFragment,inProgressFragment)
            replace(R.id.doneTableFragment, doneFragment)
            addToBackStack(null)
            commit()
        }
    }
}