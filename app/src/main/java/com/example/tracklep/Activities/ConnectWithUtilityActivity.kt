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
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("getConnectMeDetails: " + Gson().toJson(response.body()));
                        updateViews(response.body()!!.Results.Table1)
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
        }
    } else {
        //dismissDialog()
        showToast(getString(R.string.internet))
    }

    fun updateViews(data: List<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo>) {
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

}
