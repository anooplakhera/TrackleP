package com.example.tracklep.Activities

import android.os.Bundle
import android.util.Log
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
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

        txtCABtitle.text = getString(R.string.forgot_password)
        imgCABback.setOnClickListener {
            finish()
        }

        emailId = intent.getStringExtra(ApiUrls.EmailID)

        txtFirstQuestion.text = ResetPassSecurityQuestionData.getArrayItem(0).Question
        txtSecondQuestion.text = ResetPassSecurityQuestionData.getArrayItem(1).Question

        clickPerform()
    }

    private fun clickPerform() {
        btnSubmitStep2.setOnClickListener {
            if (this.editAnswer1RP.text!!.isNotEmpty() && this.editAnswer2RP.text!!.isNotEmpty()) {
                getUserReset()
            } else {
                showToast("Please Enter Correct Answers")
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
                    editAnswer2RP!!.text.toString()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.ResetPassStep2Response> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ResetPassStep2Response>,
                    response: Response<ResponseModelClasses.ResetPassStep2Response>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        Log.d("ResponseBodyIs", Gson().toJson(response.body()!!))
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

