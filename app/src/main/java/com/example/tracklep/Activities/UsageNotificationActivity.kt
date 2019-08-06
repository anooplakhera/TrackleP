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
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_usage_notification.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsageNotificationActivity : BaseActivity() {

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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUsageNotificationDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.GetUsageNotificationResponse> = apiService.getUsageNotification(
                getHeader(), AppPrefences.getLoginUserInfo(this).AccountNumber,
                ApiUrls.getJSONRequestBody(
                    RequestClass.getConnectWithUtilityRequestModel()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.GetUsageNotificationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetUsageNotificationResponse>,
                    response: Response<ResponseModelClasses.GetUsageNotificationResponse>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("getUsageNotificationDetails: " + Gson().toJson(response.body()));
                        updateViews(response.body()!!.Results.Table1)
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.GetUsageNotificationResponse>, t: Throwable) {
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
            val call: Call<ResponseModelClasses.GetUsageNotificationResponse> = apiService.getUsageNotification(
                getHeader(), AppPrefences.getLoginUserInfo(this).AccountNumber,
                ApiUrls.getJSONRequestBody(
                    RequestClass.getConnectWithUtilityRequestModel()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.GetUsageNotificationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.GetUsageNotificationResponse>,
                    response: Response<ResponseModelClasses.GetUsageNotificationResponse>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("setUsageNotificationDetails: " + Gson().toJson(response.body()));
                        updateViews(response.body()!!.Results.Table1)
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.GetUsageNotificationResponse>, t: Throwable) {
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

    fun updateViews(data: List<ResponseModelClasses.GetUsageNotificationResponse.Results1.TableTwo>) {
        /*custID = data.CustomerId.toString()
        sQuesID1 = data.SecurityQuestionId.toString()
        sQuesID2 = data.SecurityQuestionId2.toString()
        txtUserName.text = data.FullName
        editEmail.setText(data.EmailId)
        editHomePhoneNumberValue.setText(data.HomePhone)
        editMobileNumberValue.setText(data.MobilePhone)
        editAns1Value.setText(data.HintsAns)
        editAns2Value.setText(data.HintsAns2)
        txtQues1Value.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId.toString())
        txtQues2Value.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId2.toString())
        txtAccNumber.text = "Account Number : " + data.UtilityAccountNumber
        txtCommunicationAddressValue.text = data.CommunicationAddress
        txtCommunicationAddressValue.setTextColor(resources.getColor(R.color.colorBlack))*/
    }

    private fun validationField() {
        try {
            var allValid = true
            if (editMeterNumber.text!!.isEmpty()) {
                showToast("Please Enter Meter Number")
                !allValid
                return
            } else if (!editDailyLimit.text!!.isEmpty()) {
                showToast("Please Enter Daily Limit")
                !allValid
                return
            } else if (!editMonthlyLimit.text!!.isEmpty()) {
                showToast("Please Enter Monthly Limit")
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