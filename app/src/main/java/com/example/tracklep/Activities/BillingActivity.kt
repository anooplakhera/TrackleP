package com.example.tracklep.Activities

import android.os.Bundle
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.SecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class BillingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility_bill)

        getBillingDetails()
        try {
            txtCABtitle.text = getString(R.string.billing)
            imgCABback.setOnClickListener {
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getBillingDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.BillingDashboardResponse> = apiService.getBillingDashboard(
                getHeader(),
                ApiUrls.getJSONRequestBody(RequestClass.getWaterUsageRequestModel(AppPrefences.getLoginUserInfo(this).AccountNumber))
            )
            call.enqueue(object : Callback<ResponseModelClasses.BillingDashboardResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.BillingDashboardResponse>,
                    response: Response<ResponseModelClasses.BillingDashboardResponse>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("getBillingDetails: " + Gson().toJson(response.body()));
                        //updateViews(response.body()!!.Results.Table.)
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.BillingDashboardResponse>, t: Throwable) {
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

}
