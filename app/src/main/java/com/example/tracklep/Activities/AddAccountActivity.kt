package com.example.tracklep.Activities

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_add_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        clickPerform()

    }

    private fun validationFields(): Boolean {
        var isValid = true
        if (editUtilAccountNo.text!!.isEmpty()) isValid = false
        else if (editMeterNumberValue.text!!.isEmpty()) isValid = false
        else if (editPostalCode.text!!.isEmpty()) isValid = false
        return isValid
    }

    private fun clickPerform() {

        btnAddAccountSubmit.setOnClickListener {
            if (validationFields())

                addAccountAPI()
            else showToast("Please enter mandatory details.")

        }
    }

    private fun addAccountAPI() = if (Utils.isConnected(this)) {
        showDialog()

        try {
            val apiService = ApiClient.getClient(ApiUrls.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> = apiService.getLogin(
                RequestClass.getLoginRequestModel(
                    "",
                    ""
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        val loginResponseModel: ResponseModelClasses.LoginResponseModel? = response.body()
                        AppPrefences.setLoginUserInfo(this@AddAccountActivity, loginResponseModel)

                        AppLog.printLog(("Login Name " + AppPrefences.getLoginUserInfo(this@AddAccountActivity).Name))
                        AppPrefences.setLogin(this@AddAccountActivity, true)

                        startActivity(Intent(this@AddAccountActivity, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.LoginResponseModel>, t: Throwable) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

}