package com.basis.myapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View

class Util {
    companion object {
        // checks internet connection
        fun isNetworkOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val netInfo = cm!!.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
    }
}

// extension function for hiding view
fun View.hide(){
    this.visibility = View.GONE
}

// extension function for showing view
fun View.show(){
    this.visibility = View.VISIBLE
}