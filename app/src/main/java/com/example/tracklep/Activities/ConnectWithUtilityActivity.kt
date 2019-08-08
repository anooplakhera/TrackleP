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
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import kotlinx.android.synthetic.main.activity_contact_us.*
import retrofit2.Callback
import retrofit2.Response

class ConnectWithUtilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        try {
            txtCABtitle.text = getString(R.string.connect_with_utility)
            imgCABback.setOnClickListener {
                finish()
            }

            getConnectMeDetails()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getConnectMeDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.ConnectWithUtilityResponse> = apiService.getConnectWithUtility(
                getHeader(), AppPrefences.getLoginUserInfo(this).AccountNumber,
                ApiUrls.getJSONRequestBody(
                    RequestClass.getConnectWithUtilityRequestModel()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.ConnectWithUtilityResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ConnectWithUtilityResponse>,
                    response: Response<ResponseModelClasses.ConnectWithUtilityResponse>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("getConnectMeDetails: " + Gson().toJson(response.body()));
                            updateViews(response.body()!!.Results.Table3)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.ConnectWithUtilityResponse>, t: Throwable) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
            //AppPrefences.getLoginUserInfo(this).AccountNumber
        }
    } else {
        //dismissDialog()
        showToast(getString(R.string.internet))
    }

    fun updateViews(data: List<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableThree>) {

        txtContactUsEmail.text = "Email: " + data.get(0).CustomerServiceEmail
        txtContactUsPhone.text = "Phone: " + data.get(0).PrimaryPhone
        txtContactUsAddress.text = "Address: " + data.get(0).utilityAddress
        txtContactUsOpenTime.text = "Email: " + data.get(0).UtilityTime

    }

}
