package com.aquatrax.tracklep.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.ApiClient.ApiClient
import com.aquatrax.tracklep.ApiClient.ApiInterface
import com.aquatrax.tracklep.ApiClient.ApiUrls
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.aquatrax.tracklep.DataModels.ResponseModelClasses
import com.aquatrax.tracklep.R
import com.aquatrax.tracklep.Utils.AppLog
import com.aquatrax.tracklep.Utils.RequestClass
import com.aquatrax.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forgot_password_first.*
import kotlinx.android.synthetic.main.activity_forgot_password_second.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordSecondActivity : BaseActivity() {

    var emailId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_second)

        try {
            txtCABtitle.text = getString(R.string.forgot_password)
            imgCABback.setOnClickListener {
                finish()
            }

            emailId = intent.getStringExtra(ApiUrls.EmailID)

            txtFirstQuestion.text = ResetPassSecurityQuestionData.getArrayItem(0).Question
            txtSecondQuestion.text = ResetPassSecurityQuestionData.getArrayItem(1).Question

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        btnSubmitStep2.setOnClickListener {
            if (this.editAnswer1RP.text!!.isNotEmpty() && this.editAnswer2RP.text!!.isNotEmpty()) {
                getUserReset()
            } else {
                showSuccessPopup("Please enter Correct Answers")
            }
        }

    }

    private fun getUserReset() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getResetUserPass2(
                RequestClass.getForgetRequestStepTwo(
                    emailId,
                    editAnswer1RP!!.text.toString(),
                    editAnswer2RP!!.text.toString(), AppPrefences.getDataBaseInfo(this)!!
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.ResetPassStep2Response> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ResetPassStep2Response>,
                    response: Response<ResponseModelClasses.ResetPassStep2Response>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            if (response.body()!!.Table[0].STATUS != null && response.body()!!.Table[0].STATUS == "0") {
                                showSuccessPopup(response.body()!!.Table[0].Message)
                            } else {
                                AppLog.printLog("ForgetStep2reponse ", Gson().toJson(response.body()!!))
                                /*ResetPassSecurityQuestionData.clearArrayList()
                                ResetPassSecurityQuestionData.addArrayList(response.body()!!.Table)*/
                                val intent =
                                    Intent(this@ForgotPasswordSecondActivity, ForgotPasswordThirdActivity::class.java)
                                intent.putExtra(ApiUrls.EmailID, emailId)
                                startActivity(intent)
                            }
                            Log.d("ResponseBodyIs", Gson().toJson(response.body()!!))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.ResetPassStep2Response>, t: Throwable
                ) {
                    dismissDialog()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        showToast(getString(R.string.internet))
    }
}

