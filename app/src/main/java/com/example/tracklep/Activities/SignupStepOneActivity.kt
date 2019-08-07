package com.example.tracklep.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
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
import kotlinx.android.synthetic.main.activity_signupstepone.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupStepOneActivity : BaseActivity() {

    private var utilityID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupstepone)

        try {
            txtCABtitle.text = getString(R.string.registration_title_new)
            imgCABback.setOnClickListener {
                finish()
            }

            getUtilityList(false, txtUtilityName)

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            /*r_lyt_ques1.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQuestion1)
                } else {
                    getSecurityQues(true, txtQuestion1)

                }
            }*/
            r_lyt_utility.setOnClickListener {
                if (UtilitiesData.getCount() > 0) {
                    openDialog("Select Utility Name", txtUtilityName)
                } else {
                    getUtilityList(true, txtUtilityName)

                }
            }
            /*r_lyt_ques2.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQuestion2)
                } else {
                    getSecurityQues(true, txtQuestion2)
                }
            }*/

            btnSubmitRegister.setOnClickListener {
                validationField()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            if (txtUtilityName.text == getString(R.string.mandatory)) {
                showToast("Please Select Utility name")
                !allValid
                return
            } else if (editAccountNo.text!!.isEmpty()) {
                showToast("Please Enter Account Number")
                !allValid
                return
            } else if (editMeterNo.text!!.isEmpty()) {
                showToast("Please Enter Meter Number")
                !allValid
                return
            } else if (editServiceZipCode.text!!.isEmpty()) {
                showToast("Please Enter Zip Code")
                !allValid
                return
            } else if (editEmail.text!!.isEmpty()) {
                showToast("Please Enter Email")
                !allValid
                return
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString())) {
                showToast("Please Enter Valid Email")
                !allValid
                return
            } else if (editPassword.text!!.isEmpty()) {
                showToast("Please Enter Password")
                !allValid
                return
            } else if (editCPassword.text!!.isEmpty()) {
                showToast("Please Enter Confirm Password")
                !allValid
                return
            } else if (!editCPassword!!.text!!.isEmpty() && !editPassword!!.text!!.isEmpty() &&
                !editPassword.text.toString().equals(editCPassword.text.toString())
            ) {
                showToast("Password doesn't match")
                !allValid
                return
            } else if (allValid) {
                val intent =
                    Intent(this@SignupStepOneActivity, SignupStepTwoActivity::class.java)
                intent.putExtra(ApiUrls.EmailID, txtUtilityName.text.toString())
                intent.putExtra(ApiUrls.AccountNumber, editAccountNo.text.toString())
                intent.putExtra(ApiUrls.MeterNumber, editMeterNo.text.toString())
                intent.putExtra(ApiUrls.ServiceZipCode, editServiceZipCode.text.toString())
                intent.putExtra(ApiUrls.EmailID, editEmail.text.toString())
                intent.putExtra(ApiUrls.Password, editPassword.text.toString())

                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUtilityList(dialogOpen: Boolean = false, textView: TextView) = if (Utils.isConnected(this)) {
//        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getUtilityList(/*ApiUrls.AuthKey*/)
            call.enqueue(object : Callback<ResponseModelClasses.UtilityListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.UtilityListResponseModel>,
                    response: Response<ResponseModelClasses.UtilityListResponseModel>
                ) {
                    dismissDialog()
                    try {
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
                        dismissDialog()
                        AppLog.printLog("Failure()- ", t.message.toString())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
//            dismissDialog()
        }
    } else {
        showToast(getString(R.string.internet))
    }

    private fun openDialog(title: String, textView: TextView) {
        try {
            val dialog = Dialog(this@SignupStepOneActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager
            val mAdapter = UtilitiesListAdapter() { position ->
                val data = UtilitiesData.getArrayItem(position)
                utilityID = data.UtilityId.toString()
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