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
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password_first.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordFirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_first)

        txtCABtitle.text = getString(R.string.forgot_password)
        imgCABback.setOnClickListener {
            finish()
        }

        btnSubmitStep1.setOnClickListener {
            if (editEmailF1.text!!.isNotEmpty() && Utils.isValidEmail(editEmailF1.text.toString()))
                getUserEmail()
            else
                showSnackBar(this.currentFocus, "Please enter valid email")
        }

    }

    private fun getUserEmail() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getResetUserPass1(RequestClass.getForgetRequestStepOne(editEmailF1.text.toString()))
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
                            ResetPassSecurityQuestionData.clearArrayList()
                            ResetPassSecurityQuestionData.addArrayList(response.body()!!.Table)
                            val intent =
                                Intent(this@ForgotPasswordFirstActivity, ForgotPasswordSecondActivity::class.java)
                            intent.putExtra(ApiUrls.EmailID, editEmailF1.text.toString())
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
}
