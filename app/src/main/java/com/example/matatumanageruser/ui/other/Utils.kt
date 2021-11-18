package com.example.matatumanageruser.ui.other

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


fun stringFromTl(tl: TextInputLayout) = tl.editText!!.text.toString()

fun Fragment.showLongToast(message: String){
    Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show()
}

fun getDate(): String{
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Date()).toString()
}