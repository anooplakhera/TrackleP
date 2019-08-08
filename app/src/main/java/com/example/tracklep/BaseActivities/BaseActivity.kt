package com.example.tracklep.BaseActivities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.R
import com.example.tracklep.Utils.Utils

public abstract class BaseActivity : AppCompatActivity(), BaseFragment.Callback {

    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }


    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus

        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText &&
            !view.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)

                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
        }
        return super.dispatchTouchEvent(ev)
    }

    fun showToast(message: String) {
        if (message != null)
            hideKeyboard()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessPopup(message: String) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun showDialog() {
        mProgressDialog = Utils.showProgressDialog1(this)
    }

    fun dismissDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                mProgressDialog!!.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSnackBar(layout: View, msg: String) {
        Snackbar.make(layout, msg, Snackbar.LENGTH_LONG).show()
    }

    fun getHeader(): String {
        return AppPrefences.getLoginUserInfo(this).token_type + " " + AppPrefences.getLoginUserInfo(this).access_token
    }

}