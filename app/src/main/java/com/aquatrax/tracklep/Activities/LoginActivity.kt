package com.aquatrax.tracklep.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.Adapter.UtilitiesListAdapter
import com.aquatrax.tracklep.ApiClient.ApiClient
import com.aquatrax.tracklep.ApiClient.ApiInterface
import com.aquatrax.tracklep.ApiClient.ApiUrls
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.DataClasses.UtilitiesData
import com.aquatrax.tracklep.DataModels.ResponseModelClasses
import com.aquatrax.tracklep.R
import com.aquatrax.tracklep.Utils.AppLog
import com.aquatrax.tracklep.Utils.RequestClass
import com.aquatrax.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    var tanentId = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            clickPerform()
            getUtilityList(false, textUtilities)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        if (AppPrefences.getRememberMe(this)!!) {
            editUserName.setText(AppPrefences!!.getUserID(this))
            editUserPass.setText(AppPrefences!!.getPassword(this))
            textUtilities.text = AppPrefences!!.getUtilityName(this)
            switchBtn.isChecked = true
        }
    }

    private fun validationFields() {
        try {
            var isValid = true
            if (editUserName.text!!.isEmpty() && editUserPass.text!!.isEmpty()) {
                showSuccessPopup("Please enter Email/Password")
                !isValid
                return
            } else if (editUserName.text!!.isEmpty()) {
                showSuccessPopup("Please enter valid Email")
                !isValid
                return
            } else if (editUserPass.text!!.isEmpty()) {
                showSuccessPopup("Password cannot be empty")
                !isValid
                return
            } else if (editUserPass.text!!.isEmpty() || !isPasswordValid(editUserPass.text)) {
                showSuccessPopup(getString(R.string.password_validation_message))
                !isValid
                return
            } else if (textUtilities.text.toString() == getString(R.string.select_utility)) {
                showSuccessPopup("Please select Water District/ Agency")
                !isValid
                return
                /* } else if (!switchBtn.isChecked) {
                     showSuccessPopup("Please enable Remember me")
                     !isValid
                     return*/
            } else if (isValid) {
                if (switchBtn.isChecked) {
                    loginApi(true)
                } else {
                    loginApi(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            textUtilities.setOnClickListener {
                if (UtilitiesData.getCount() > 0) {
                    openDialog(getString(R.string.select_utility), textUtilities)
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
                alertDialog.setMessage("You will be redirected to " + textUtilities.text.toString() + " online payment system. Click Proceed to continue.")
                alertDialog.setNeutralButton("Cancel") { _, _ ->
                }

                alertDialog.setPositiveButton("Proceed") { dialog, which ->
                    dialog.dismiss()
                    startWebActivity("Pay Bill", "https://eocwd.epayub.com")
                }

                alertDialog.show()
            }

//        editUserName.setText("utkarsh3441@gmail.com")
            /*editUserName.setText("amitsh@hotmail.com")
            editUserPass.setText("Trackle@999")*/

            btnLogin.setOnClickListener {
                validationFields()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun loginApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> = apiService.getLogin(
                RequestClass.getLoginRequestModel(
                    editUserName.text.toString(),
                    editUserPass.text.toString(),
                    tanentId, AppPrefences.getDataBaseInfo(this)!!
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {

                            var loginResponseModel: ResponseModelClasses.LoginResponseModel? =
                                response.body()
                            AppPrefences.setLoginUserInfo(this@LoginActivity, loginResponseModel)
                            AppPrefences.setAccountNumber(
                                this@LoginActivity,
                                loginResponseModel!!.AccountNumber
                            )
                            AppPrefences.setUtilityAccountNumber(
                                this@LoginActivity,
                                loginResponseModel!!.UtilityAccountNumber
                            )

                            AppLog.printLog("loginApiResponse: " + Gson().toJson(response.body()));
                            AppLog.printLog("loginApiResponse: " + Gson().toJson(response.code()));
                            AppPrefences.setLogin(this@LoginActivity, true)

                            if (isChecked) {
                                AppPrefences.setRememberMe(this@LoginActivity, true)
                                AppPrefences.setUserID(
                                    this@LoginActivity,
                                    editUserName.text.toString()
                                )
                                AppPrefences.setPassword(
                                    this@LoginActivity,
                                    editUserPass.text.toString()
                                )
                                AppPrefences.setUtilityName(
                                    this@LoginActivity,
                                    textUtilities.text.toString()
                                )
                            } else {
                                AppPrefences.setRememberMe(this@LoginActivity, false)
                            }

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            AppLog.printLog("loginApiErrorResponse: " + response.errorBody())
                            showSuccessPopup(response.message())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    t: Throwable
                ) {
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

    private fun getUtilityList(dialogOpen: Boolean = false, textView: TextView) =
        if (Utils.isConnected(this)) {
            showDialog()
            try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
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
                            val data = UtilitiesData.getArrayItem(0)
                            textView.text = data.Name
                            textView.setTextColor(resources.getColor(R.color.colorBlack))
                            AppPrefences.setDataBaseInfo(
                                this@LoginActivity,
                                ResponseModelClasses.DataBaseUtils(
                                    data.ServerName,
                                    data.DataBaseName,
                                    data.UserName,
                                    data.Password
                                )
                            )
                            if (dialogOpen) {
                                openDialog(getString(R.string.select_utility), textView)
                            }
                            AppLog.printLog(
                                "UtilityList Response- ",
                                Gson().toJson(response.body())
                            )


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
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager as RecyclerView.LayoutManager?

            val itemDecor = DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
            dialog.dialogRecycleView.addItemDecoration(itemDecor)

            val mAdapter = UtilitiesListAdapter() { position ->
                val data = UtilitiesData.getArrayItem(position)
                textView.text = data.Name
                tanentId = data.UtilityId.toString()
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                AppPrefences.setDataBaseInfo(
                    this@LoginActivity,
                    ResponseModelClasses.DataBaseUtils(
                        data.ServerName,
                        data.DataBaseName,
                        data.UserName,
                        data.Password
                    )
                )
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}