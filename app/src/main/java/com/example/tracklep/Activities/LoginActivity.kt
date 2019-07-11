package com.example.tracklep.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppConstants
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        clickPerform()

    }

    private fun validationFields(): Boolean {
        var isValid = true
        if (editUserName.text!!.isEmpty()) isValid = false
        else if (editUserPass.text!!.isEmpty()) isValid = false
        return isValid
    }

    private fun clickPerform() {
        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordFirstActivity::class.java))
        }
        lytRegisterUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
        editUserName.setText("utkarsh3441@gmail.com")
        editUserPass.setText("Trackle@999")

        btnLogin.setOnClickListener {
            if (validationFields())
                if (switchBtn.isChecked) {
                    loginApi()
                } else showToast("Please Enable Remember me")
            else showToast("Please enter correct username & password")

        }
    }


    private fun loginApi() = if (Utils.isConnected(this)) {
        try {
            var map = HashMap<String, String>()
//            map.put(ApiUrls.UserName, "utkarsh3441@gmail.com");
//            map.put(ApiUrls.Password, "Trackle@999");
            map.put(ApiUrls.UserName, editUserName.text.toString());
            map.put(ApiUrls.Password, editUserPass.text.toString());
            map.put(ApiUrls.GrantType, ApiUrls.Password.toLowerCase());
            map.put(ApiUrls.TanentId, "1");
            map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
            map.put(ApiUrls.Database, ApiUrls.Database_value)
            map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
            map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
            val apiService = ApiClient.getClient(ApiUrls.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> = apiService.getLogin(map)
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    if (response.body() != null) {
                        AppLog.printLog("Login Response: ", response.body().toString())

                        AppConstants.loginResponseModel = response.body()
                        AppLog.printLog(AppConstants.loginResponseModel!!.Name)
                        AppPrefences.setLogin(this@LoginActivity, true)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.LoginResponseModel>, t: Throwable) {
                    // Log error here since request failed
                    AppLog.printLog("Failure()- ", t.message.toString())
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

}
