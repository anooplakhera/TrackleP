package com.example.tracklep.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.Adapter.UtilitiesListAdapter
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.UtilitiesData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        clickPerform()
        getUtilityList(false, textUtilities)

    }

    private fun validationFields(): Boolean {
        var isValid = true
        if (editUserName.text!!.isEmpty()) isValid = false
        else if (editUserPass.text!!.isEmpty()) isValid = false
        return isValid
    }

    private fun clickPerform() {
        textUtilities.setOnClickListener {
            if (UtilitiesData.getCount() > 0) {
                openDialog("Select Utility", textUtilities)
            } else {
                getUtilityList(true, txtQuestion1)
            }
        }
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
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> = apiService.getLogin(
                RequestClass.getLoginRequestModel(
                    editUserName.text.toString(),
                    editUserPass.text.toString()
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
                        AppPrefences.setLoginUserInfo(this@LoginActivity, loginResponseModel)

                        AppLog.printLog(("Login Name " + AppPrefences.getLoginUserInfo(this@LoginActivity).Name))
                        AppPrefences.setLogin(this@LoginActivity, true)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
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

    private fun getUtilityList(dialogOpen: Boolean = false, textView: TextView) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getUtilityList(/*ApiUrls.AuthKey*/)
            call.enqueue(object : Callback<ResponseModelClasses.UtilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.UtilityListResponseModel>,
                    response: Response<ResponseModelClasses.UtilityListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null)
                            UtilitiesData.clearArrayList()
                        UtilitiesData.addArrayList(response.body()!!.Results.Table)
                        if (dialogOpen) {
                            openDialog("Select Utilities", textView)
                        }
                        AppLog.printLog("UtilityList Response- ", response.body().toString())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.UtilityListResponseModel>,
                    t: Throwable
                ) {
                    try {
                        AppLog.printLog("Failure()- ", t.message.toString())
                        dismissDialog()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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

    private fun openDialog(title: String, textView: TextView) {
        try {
            val dialog = Dialog(this@LoginActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager as RecyclerView.LayoutManager?
            val mAdapter = UtilitiesListAdapter() { position ->
                val data = UtilitiesData.getArrayItem(position)
                textView.text = data.Name
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
