package com.aquatrax.tracklep.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.ApiClient.ApiClient
import com.aquatrax.tracklep.ApiClient.ApiInterface
import com.aquatrax.tracklep.ApiClient.ApiUrls
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.DataModels.ResponseModelClasses
import com.aquatrax.tracklep.R
import com.aquatrax.tracklep.Utils.AppConstants
import com.aquatrax.tracklep.Utils.AppLog
import com.aquatrax.tracklep.Utils.RequestClass
import com.aquatrax.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        txtCABtitle.text = getString(R.string.add_account)
        imgCABadd.visibility = View.GONE
        imgCABback.setOnClickListener {
            finish()
        }
        clickPerform()

    }


    private fun validationFields(): Boolean {
        var isValid = true
        try {
            if (editUtilAccountNo.text!!.isEmpty() &&
                editMeterNumberValue.text!!.isEmpty() &&
                editPostalCode.text!!.isEmpty()
            ) {
                isValid = false
                showSuccessPopup("Please enter mandatory details.")
            } else if (editUtilAccountNo.text!!.isEmpty() || editUtilAccountNo.length() < 7) {
                showSuccessPopup("Please enter Utility Account Number.")
                isValid = false
            } else if (editMeterNumberValue.text!!.isEmpty() || editMeterNumberValue.length() < 8) {
                showSuccessPopup("Please enter valid Meter Number.")
                isValid = false
            } else if (editPostalCode.text!!.isEmpty() || editPostalCode.length() < 5) {
                showSuccessPopup("Please enter valid Postal Code.")
                isValid = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isValid
    }

    private fun clickPerform() {

        btnAddAccountSubmit.setOnClickListener {
            if (validationFields())

                addAccountAPI()
            //else showSuccessPopup("Please enter mandatory details.")

        }
    }

    private fun addAccountAPI() = if (Utils.isConnected(this)) {
        showDialog()

        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.AddAccount> = apiService.setAddAccount(
                getHeader(),
                RequestClass.getAddAccountRequestModel(
                    editUtilAccountNo.text.toString(),
                    editPostalCode.text.toString(),
                    editMeterNumberValue.text.toString(), AppPrefences.getDataBaseInfo(this)!!
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.AddAccount> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.AddAccount>,
                    response: Response<ResponseModelClasses.AddAccount>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {

                            /*val loginResponseModel: ResponseModelClasses.AddAccount? = response.body()
                            AppPrefences.setProfileInfo(this@AddAccountActivity, loginResponseModel)

                            AppLog.printLog(("Login Name " + AppPrefences.getLoginUserInfo(this@AddAccountActivity).Name))
                            AppPrefences.setLogin(this@AddAccountActivity, true)

                            startActivity(Intent(this@AddAccountActivity, MainActivity::class.java))
                            finish()*/
                            editUtilAccountNo.text?.clear()
                            editPostalCode.text?.clear()
                            editMeterNumberValue.text?.clear()

                            var alertDialog = AlertDialog.Builder(this@AddAccountActivity)
                            alertDialog.setTitle(getString(R.string.app_name))
                            alertDialog.setMessage(response.body()!!.Message)

                            alertDialog.setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                                AppConstants.IsAccountAdded = true
                                finish()
                            }

                            alertDialog.show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.AddAccount>, t: Throwable) {
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