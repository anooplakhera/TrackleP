package com.example.tracklep.Activities

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_forgot_password_third.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordThirdActivity : BaseActivity() {

    var emailId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_third)

        txtCABtitle.text = getString(R.string.forgot_password)
        imgCABback.setOnClickListener {
            finish()
        }

        emailId = intent.getStringExtra(ApiUrls.EmailID)

        btnSubmitStep1.setOnClickListener {
            validationField()
        }

    }

    private fun getChangePassword() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getResetUserPass3(
                RequestClass.getForgetRequestStepThree(
                    emailId, editPassword.text.toString(),
                    editConfirmPassword.text.toString()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.ResetPassStep3Response> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ResetPassStep3Response>,
                    response: Response<ResponseModelClasses.ResetPassStep3Response>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            if (response.body()!!.Table[0].STATUS != null && response.body()!!.Table[0].STATUS == "0") {
                                showSuccessPopup(response.body()!!.Table[0].Message)
                            } else {
                                AppLog.printLog("ForgetStep1reponse ", Gson().toJson(response.body()!!))
                                /*ResetPassSecurityQuestionData.clearArrayList()
                                ResetPassSecurityQuestionData.addArrayList(response.body()!!.Table)
                                val intent =
                                    Intent(this@ForgotPasswordSecondActivity, ForgotPasswordThirdActivity::class.java)
                                intent.putExtra(ApiUrls.EmailID, editEmailF1.text.toString())
                                startActivity(intent)*/
                            }
                            Log.d("ResponseBodyIs", Gson().toJson(response.body()!!))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.ResetPassStep3Response>, t: Throwable
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
            if (editPassword.text!!.isEmpty() || !isPasswordValid(editPassword.text)) {
                showSuccessPopup(getString(R.string.password_validation_message))
                !allValid
                return
            } else if (editConfirmPassword.text!!.isEmpty() || !isPasswordValid(editConfirmPassword.text)) {
                showSuccessPopup("Please confirm password")
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
