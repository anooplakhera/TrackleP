package com.example.tracklep.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.view.Gravity
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_menu_layout.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgNavIcon.setOnClickListener {
            if (!drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.openDrawer(Gravity.RIGHT);
            else drawer_layout.closeDrawer(Gravity.END);
        }

        navigationClick()
        clickPerform()

    }

    private fun navigationClick() {
        lytCompanyWebsite.setOnClickListener {
            closeDrawer()
        }
        lytPayLocation.setOnClickListener {
            closeDrawer()
        }
        lytVisitFb.setOnClickListener {
            closeDrawer()
        }
        lytVisitTwitter.setOnClickListener {
            closeDrawer()
        }
        lytPrivacyPolicy.setOnClickListener {
            closeDrawer()
        }
        lytLogout.setOnClickListener {
            userLogOut()
        }
    }

    private fun closeDrawer() {
        Handler().postDelayed(Runnable {
            drawer_layout.closeDrawers()
        }, 200)
    }

    private fun clickPerform() {
        lytAccount.setOnClickListener {
            startActivity(Intent(this, MyProfile::class.java))
        }
        lytBilling.setOnClickListener {}
        lytConnectUtility.setOnClickListener {}
        lytTrackUsage.setOnClickListener {}
        lytCompare.setOnClickListener {}
        lytWaterConversation.setOnClickListener {}
    }


    private fun userLogOut() {
        closeDrawer()
        logoutAlertDialog()
    }

    fun logoutAlertDialog() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            AppPrefences.clearAll(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }
}
