package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardClosed():Boolean{
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    return !imm.isActive
}

fun Activity.isKeyboardOpen():Boolean{
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isActive
}