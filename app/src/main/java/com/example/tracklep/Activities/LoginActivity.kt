package com.example.tracklep.Activities

import android.app.AlertDialog
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
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v7.widget.DividerItemDecoration
import android.widget.LinearLayout.HORIZONTAL
import com.example.tracklep.R


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        clickPerform()
        getUtilityList(false, textUtilities)

    }

    private fun validationFields() {
        var isValid = true
        if (editUserName.text!!.isEmpty()) {
            !isValid
            return
        } else if (editUserPass.text!!.isEmpty()) {
            !isValid
            return
        } else if (textUtilities.text.toString() == getString(R.string.select_utility)) {
            showToast("Please Select Utility")
            !isValid
            return
        } else if (!switchBtn.isChecked) {
            showToast("Please Enable Remember me")
            !isValid
            return
        } else if (isValid) {
            loginApi()
        }
    }

    private fun clickPerform() {
        textUtilities.setOnClickListener {
            if (UtilitiesData.getCount() > 0) {
                openDialog("Select Utility Name", textUtilities)
            } else {
                getUtilityList(true, textUtilities)
            }
        }

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordFirstActivity::class.java))
        }

        lytRegisterUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupStepOneActivity::class.java))
        }

        lytConnectWithAgency.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ConnectWithUtilityActivity::class.java))
        }

        lytPayBill.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.app_name))
            alertDialog.setMessage("You will be redirected to EOCWD online payment system. Click Proceed to continue.")
            alertDialog.setNeutralButton("Cancel") { _, _ ->
            }

            alertDialog.setPositiveButton("Proceed") { dialog, which ->
                dialog.dismiss()
                startWebActivity("Pay Bill", "https://eocwd.epayub.com")
            }

            alertDialog.show()
        }

//        editUserName.setText("utkarsh3441@gmail.com")
        editUserName.setText("amitsh@hotmail.com")
        editUserPass.setText("Trackle@999")

        btnLogin.setOnClickListener {
            validationFields()
        }
    }

    private fun startWebActivity(title: String, url: String) {
        try {
            intent = Intent(this@LoginActivity, WebViewActivity::class.java)
            intent.putExtra("contentTitle", title)
            intent.putExtra("contentUrl", "https://eocwd.epayub.com")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
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
                        var loginResponseModel: ResponseModelClasses.LoginResponseModel? = response.body()
                        AppPrefences.setLoginUserInfo(this@LoginActivity, loginResponseModel)
                        AppPrefences.setAccountNumber(this@LoginActivity, loginResponseModel!!.AccountNumber)
                        AppLog.printLog("loginApiResponse: " + Gson().toJson(response.body()));
                        AppLog.printLog(("Login Name " + AppPrefences.getLoginUserInfo(this@LoginActivity)!!.Name))
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
                            openDialog("Select Utility Name", textView)
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

            val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            dialog.dialogRecycleView.addItemDecoration(itemDecor)

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