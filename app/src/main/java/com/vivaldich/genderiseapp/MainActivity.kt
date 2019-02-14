package com.vivaldich.genderiseapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.vivaldich.genderiseapp.model.Gender
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent()
    }

    private fun initComponent() {
        editFirstName.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                retrieveGenderProbability(editFirstName.text.toString())
                return@OnEditorActionListener true
            }

            false
        })

        editFirstName.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {
                retrieveGenderProbability(editFirstName.text.toString())
                return@OnKeyListener true
            }

            false
        })
    }

    private fun retrieveGenderProbability(name: String) {
        Log.i(TAG, name)

        // TODO: call retrofit API here
    }

    private fun showGenderAndProbability(gender: Gender) {
        // TODO: set textView to visible and edit text accordingly
    }
}
