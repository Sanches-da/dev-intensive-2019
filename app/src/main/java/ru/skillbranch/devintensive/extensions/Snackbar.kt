package ru.skillbranch.devintensive.extensions

import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.skillbranch.devintensive.R

fun Snackbar.applyTextColor():Snackbar{
    val tv = this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val attrs = intArrayOf(android.R.attr.textColor)
    val ta = this.context.obtainStyledAttributes(R.style.Snackbar_Text, attrs)

    tv.setTextColor(ta.getColor(0, this.context.getColor(android.R.color.white)))

    ta.recycle()

    return this
}