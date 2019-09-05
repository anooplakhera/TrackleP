package com.example.tracklep.Activities

import android.os.Bundle
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_usage_notification.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsageNotificationActivity : BaseActivity() {

    private var unit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage_notification)

        try {
            txtCABtitle.text = getString(R.string.usage_notification)
            imgCABback.setOnClickListener {
                finish()
            }

            getUsageNotificationDetails()



            btnUsageNotificationSubmit.setOnClickListener {
                validationField()
            }
            rbn_gallon.setOnClickListener {
                unit = "Gallon"
                rbn_ccf.isChecked = false
                rbn_gallon.isChecked = true
            }
            rbn_ccf.setOnClickListener {
                unit = "CCF"
                rbn_ccf.isChecked = true
                rbn_gallon.isChecked = false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUsageNotificationDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>> =
                apiService.getUsageNotification(
                    getHeader(),
                    AppPrefences.getAccountNumber(this),
                    ApiUrls.getJSONRequestBody(
                        RequestClass.getUsageNotificationRequestModel(
                            AppPrefences.getAccountNumber(this),AppPrefences.getDataBaseInfo(this)!!)
                    )
                )
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>>,
                    response: Response<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("getUsageNotificationDetails: " + Gson().toJson(response.body()));
                        updateViews(response.body()!!.get(0))
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>>,
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

    private fun setUsageNotificationDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.UpdateUsageNotification> = apiService.setUsageNotification(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.setUsageNotificationRequestModel(
                        editMonthlyLimit.text.toString(),
                        editDailyLimit.text.toString(),
                        editMeterNumber.text.toString(),
                        AppPrefences.getAccountNumber(this),
                        unit,AppPrefences.getDataBaseInfo(this)!!
                    )
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.UpdateUsageNotification> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.UpdateUsageNotification>,
                    response: Response<ResponseModelClasses.UpdateUsageNotification>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("setUsageNotificationDetails: " + Gson().toJson(response.body()));
                        showSuccessPopup(response.body()!!.Message)
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.UpdateUsageNotification>, t: Throwable) {
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

    fun updateViews(data: ResponseModelClasses.GetUsageNotificationResponse) {

        editMeterNumber.setText(data.MeterNumber)
        editDailyLimit.setText(data.DailyThreshold)
        editMonthlyLimit.setText(data.MonthlyThreshold)
        if (data.Unit == "CCF") {
            rbn_ccf.isChecked = true
            rbn_gallon.isChecked = false
            unit = "CCF"
        } else {
            rbn_ccf.isChecked = false
            rbn_gallon.isChecked = true
            unit = "Gallon"
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            if (editMeterNumber.text!!.isEmpty()) {
                showToast("Please enter Meter Number")
                !allValid
                return
            } else if (editDailyLimit.text!!.isEmpty()) {
                showToast("Please enter Daily Limit")
                !allValid
                return
            } else if (editMonthlyLimit.text!!.isEmpty()) {
                showToast("Please enter Monthly Limit")
                !allValid
                return
            } else if (allValid) {
                setUsageNotificationDetails()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}