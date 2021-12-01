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

fun getFirstDayOfTheMonth(): String{
    return getYear().toString()+"-"+ getMonth()+"-1 00:00:00.0000"

}

fun getEndOfToday(): String{
    return getYear().toString()+"-"+ getMonth()+"-"+ getDay()+" 23:59:59.0000"

}

fun getDay(): Int{
    val sdf = SimpleDateFormat("dd")
    return sdf.format(Date()).toInt()
}

fun getMonth(): Int{
    val sdf = SimpleDateFormat("M")
    return sdf.format(Date()).toInt()
}

fun getYear(): Int{
    val sdf = SimpleDateFormat("yyyy")
    return sdf.format(Date()).toInt()
}


