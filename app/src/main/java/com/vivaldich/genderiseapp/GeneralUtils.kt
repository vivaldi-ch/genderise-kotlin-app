package com.vivaldich.genderiseapp

import android.support.design.widget.Snackbar
import android.view.View

object GeneralUtils {
    @JvmStatic
    fun showSnackBar(parentView: View, message: String?) {
        val messageDefault = message ?: "Error encountered. Please try again."
        Snackbar.make(parentView, messageDefault, Snackbar.LENGTH_LONG).show()
    }

    @JvmStatic
    fun isStringValid(string: String?): Boolean {
        return string != null && !string.isEmpty()
    }
}