package com.example.tracklep.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.View
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.UserMeterListData
import com.example.tracklep.DataClasses.WaterUsageData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.StatusBar
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_menu_layout.*
import pl.droidsonroids.gif.AnimationListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MainActivity : BaseActivity(), AnimationListener {
    override fun onAnimationCompleted(loopNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mType = "G"
    private var mMode = "D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBar.darkenStatusBar(this, R.color.colorBlack)
        navigationClick()
        clickPerform()
        imgNavIcon.setOnClickListener {
            if (!drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.openDrawer(Gravity.RIGHT);
            else drawer_layout.closeDrawer(Gravity.END);
        }
        try {

            txtDashTitle.text = "Welcome " + AppPrefences.getLoginUserInfo(this)!!.Name

            getMeterDetailsAMI()

            setMeterData(AppPrefences.getMeterUsageData(this@MainActivity))

//            var gifDrawable = GifDrawable(resources, R.drawable.waterflow567)
//            gifDrawable.stop();
//            gifDrawable.addAnimationListener(this);
//            gifViewer.setImageDrawable(gifDrawable)
//            gifDrawable.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigationClick() {
        try {
            lytCompanyWebsite.setOnClickListener {
                closeDrawer()
                startWebActivity(
                    "Company Website",
                    AppPrefences.getLoginUserInfo(this)!!.CompanyWebsite
                )
            }
            lytPayLocation.setOnClickListener {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(getString(R.string.app_name))
                alertDialog.setMessage("You will be redirected to EOCWD online payment system. Click Proceed to continue.")
                alertDialog.setNeutralButton("Cancel") { _, _ ->
                }

                alertDialog.setPositiveButton("Proceed") { dialog, which ->
                    dialog.dismiss()
                    startWebActivity("Pay Bill", "https://eocwd.epayub.com")
                }

                alertDialog.show()
            }
            lytVisitFb.setOnClickListener {
                closeDrawer()
                startWebActivity("Facebook", AppPrefences.getLoginUserInfo(this)!!.FacebookUrl)
            }
            lytVisitTwitter.setOnClickListener {
                closeDrawer()
                startWebActivity("Twitter", AppPrefences.getLoginUserInfo(this)!!.TwitterURL)
            }
            lytPrivacyPolicy.setOnClickListener {
                closeDrawer()
                startWebActivity(
                    "Privacy Policy",
                    AppPrefences.getLoginUserInfo(this)!!.PrivacyPolicy
                )
            }
            lytLogout.setOnClickListener {
                userLogOut()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startWebActivity(title: String, url: String) {
        try {
            intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra("contentTitle", title)
            intent.putExtra("contentUrl", url)
            startActivity(intent)
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
        try {
            lytAccount.setOnClickListener(View.OnClickListener {

                startActivity(Intent(this, MyProfile::class.java))
            })
//        lytAccount.setOnClickListener {
//            startActivity(Intent(this, MyProfile::class.java))
//        }
            lytBilling.setOnClickListener {
                startActivity(Intent(this, BillingActivity::class.java))
            }
            lytConnectUtility.setOnClickListener {

                startActivity(Intent(this, ConnectWithUtilityActivity::class.java))
                /*val mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_contact_us, null)
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
                }*/
            }

            lytTrackUsage.setOnClickListener {
                startActivity(Intent(this, UsageActivity::class.java))
            }
            lytCompare.setOnClickListener {
                startActivity(Intent(this, CompareActivity::class.java))
            }
            lytWaterConversation.setOnClickListener {
                startActivity(Intent(this, ConservationActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun userLogOut() {
        closeDrawer()
        logoutAlertDialog()
    }

    private fun logoutAlertDialog() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            //AppPrefences.clearAll(this)

            AppPrefences.setLogin(this, false)
            AppPrefences.setAccountNumber(this, "")
            AppPrefences.setIsAMI(this, false)
            AppPrefences.setUtilityAccountNumber(this, "")

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }

    private fun getMeterDetailsAMI() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.MeterDetails> = apiService.getMeterDetails(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getMeterDetailsRequestModel(
                        AppPrefences.getAccountNumber(this), AppPrefences.getDataBaseInfo(this)!!
                    )
                ),
                AppPrefences.getAccountNumber(this)
            )
            call.enqueue(object : Callback<ResponseModelClasses.MeterDetails> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.MeterDetails>,
                    response: Response<ResponseModelClasses.MeterDetails>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            UserMeterListData.clearArrayList()
                            UserMeterListData.addArrayList(response.body()!!.Results.Table)
                            getWaterUsage()
                            AppLog.printLog("MeterDetailsResponse: " + Gson().toJson(response.body()));
                            AppPrefences.setIsAMI(
                                this@MainActivity,
                                response.body()!!.Results.Table.get(0).IsAMI
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.MeterDetails>,
                    t: Throwable
                ) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        //dismissDialog()
        showToast(getString(R.string.internet))
    }


    private fun getWaterUsage() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.WaterUsages> = apiService.getWaterUsages(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getWaterUsageRequestModel(
                        AppPrefences.getAccountNumber(this),
                        mType,
                        mMode,
                        AppPrefences.getDataBaseInfo(this)!!
                    )
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.WaterUsages> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.WaterUsages>,
                    response: Response<ResponseModelClasses.WaterUsages>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("WaterDetails: " + Gson().toJson(response.body()));
                            if (response.body()!!.Results.Table != null && response.body()!!.Results.Table.size > 0) {
                                var data =
                                    ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>()
                                data.addAll(response.body()!!.Results.Table)
                                data.reverse()
                                WaterUsageData.clearArrayList()
                                WaterUsageData.addArrayList(data)

                                AppPrefences.setMeterUsageData(this@MainActivity, data[0])

                                setMeterData(AppPrefences.getMeterUsageData(this@MainActivity))
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.WaterUsages>, t: Throwable) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        //dismissDialog()
        showToast(getString(R.string.internet))
    }

    fun setMeterData(data: ResponseModelClasses.WaterUsages.Results1.TableOne) {
        try {
            txtUsagesMessage.text =
                "You have consumed " + data.AVERAGE + " Gallons water so far this calendar month"
            txtMeterValue.text = data.AVERAGE + "\nGallons"
            arc_progress.progress =
                Utils.getProgressValue(data.AllocationValue.toDouble(), data.AVERAGE.toDouble())
                    .roundToInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
