package com.vivaldich.genderiseapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.gson.Gson
import com.vivaldich.genderiseapp.model.Gender
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private var mCompositeDisposable: CompositeDisposable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDisposable()
        initComponent()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    private fun initComponent() {
        editFirstName.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
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

    private fun initDisposable() {
        mCompositeDisposable = CompositeDisposable();
    }

    private fun retrieveGenderProbability(name: String) {
        if (!GeneralUtils.isStringValid(name)) {
            GeneralUtils.showSnackBar(parentView, "Please enter a valid name")
            return
        }

        progressBar.visibility = View.VISIBLE

        mCompositeDisposable?.add(
            WebApiClient.instance.get(getFirstWord(name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::showGenderAndProbability, this::handleError)
        )
    }

    private fun showGenderAndProbability(gender: Gender) {
        if (isGenderValid(gender)) {
            textGender.text = gender.gender!!.capitalize()
            textGender.visibility = View.VISIBLE
        } else {
            textGender.visibility = View.INVISIBLE
        }

        textYouAre.text = parseGenderProbability(gender)
        textYouAre.visibility = View.VISIBLE

        progressBar.visibility = View.GONE
    }

    private fun parseGenderProbability(gender: Gender): String {
        if (!GeneralUtils.isStringValid(gender.probability)) {
            return "Sorry, your gender is indeterminate..."
        }

        return "There is a ${Math.round(gender.probability!!.toFloat() * 100)}% chance that you are a..."
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()

        progressBar.visibility = View.GONE
        GeneralUtils.showSnackBar(parentView, error.message)
    }

    private fun isGenderValid(gender: Gender): Boolean {
        return GeneralUtils.isStringValid(gender.probability) &&
                GeneralUtils.isStringValid(gender.gender)
    }

    private fun getFirstWord(name: String?): String {
        if (name == null) return ""

        return name.split(" ")[0]
    }
}
