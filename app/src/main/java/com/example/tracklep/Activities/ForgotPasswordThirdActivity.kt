package com.example.tracklep.Activities

import android.content.Intent
import android.os.Bundle
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forgot_password_third.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordThirdActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_third)

        txtCABtitle.text = getString(R.string.forgot_password)
        imgCABback.setOnClickListener {
            finish()
        }

        btnSubmitStep1.setOnClickListener {
            validationField()
        }

    }

    private fun getChangePassword() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getResetUserPass1(RequestClass.getForgetRequestStepOne(editPassword.text.toString()))
            call.enqueue(object : Callback<ResponseModelClasses.ResetPassStep1Response> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ResetPassStep1Response>,
                    response: Response<ResponseModelClasses.ResetPassStep1Response>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        if (response.body()!!.Table[0].Status != null && response.body()!!.Table[0].Status == "0") {
                            showToast(response.body()!!.Table[0].Message)
                        } else {
                            AppLog.printLog("ForgetStep1reponse ", Gson().toJson(response.body()!!))
                            ResetPassSecurityQuestionData.clearArrayList()
                            ResetPassSecurityQuestionData.addArrayList(response.body()!!.Table)
                            val intent =
                                Intent(this@ForgotPasswordThirdActivity, ForgotPasswordSecondActivity::class.java)
                            intent.putExtra(ApiUrls.EmailID, editPassword.text.toString())
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.ResetPassStep1Response>, t: Throwable
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

    private fun validationField() {
        try {
            var allValid = true
            if (editPassword.text.toString() == getString(R.string.select_topic)) {
                showToast("Please enter Password")
                !allValid
                return
            } else if (editConfirmPassword.text!!.isEmpty()) {
                showToast("Please confirm password")
                !allValid
                return
            } else if (allValid) {
                getChangePassword()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
