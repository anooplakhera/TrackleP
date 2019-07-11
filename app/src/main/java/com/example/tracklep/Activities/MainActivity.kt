package com.example.tracklep.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.LayoutInflater
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.R
import com.example.tracklep.Utils.AppConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_contact_us.view.*
import kotlinx.android.synthetic.main.navigation_menu_layout.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            imgNavIcon.setOnClickListener {
                if (!drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.openDrawer(Gravity.RIGHT);
                else drawer_layout.closeDrawer(Gravity.END);
            }

            navigationClick()
            clickPerform()
            txtDashTitle.setText("Welcome " + AppConstants.loginResponseModel!!.Name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigationClick() {
        try {
            lytCompanyWebsite.setOnClickListener {
                try {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("contentTitle", "Company Website")
                    intent.putExtra("contentUrl", AppConstants.loginResponseModel!!.CompanyWebsite)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            lytPayLocation.setOnClickListener {
                closeDrawer()
            }
            lytVisitFb.setOnClickListener {
                try {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("contentTitle", "Facebook")
                    intent.putExtra("contentUrl", AppConstants.loginResponseModel!!.FacebookUrl)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            lytVisitTwitter.setOnClickListener {
                try {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("contentTitle", "Twitter")
                    intent.putExtra("contentUrl", AppConstants.loginResponseModel!!.TwitterURL)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            lytPrivacyPolicy.setOnClickListener {
                try {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("contentTitle", "Privacy Policy")
                    intent.putExtra("contentUrl", AppConstants.loginResponseModel!!.PrivacyPolicy)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            lytLogout.setOnClickListener {
                userLogOut()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        lytBilling.setOnClickListener {
            startActivity(Intent(this, BillingDashboard::class.java))
        }
        lytConnectUtility.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact_us, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            //show dialog
            val mAlertDialog = mBuilder.show()

            mDialogView.txtContactUsPhone.setOnClickListener {

                mAlertDialog.dismiss()
            }
            mDialogView.txtContactUsOnline.setOnClickListener {

                try {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra("contentTitle", "ContactUs")
                    intent.putExtra("contentUrl", "www.eocwd.com/contact")
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

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
