package com.example.tracklep.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tracklep.Adapter.QuestionListAdapter
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.SecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        try {
            txtCABtitle.text = getString(R.string.registration_title)
            imgCABback.setOnClickListener {
                finish()
            }

            getSecurityQues(false, txtQuestion2)
            getUtilityList(false, txtUtilityName)

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun clickPerform() {
        try {
            r_lyt_ques1.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQuestion1)
                } else {
                    getSecurityQues(true, txtQuestion1)

                }
            }
            r_lyt_ques2.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQuestion2)
                } else {
                    getSecurityQues(true, txtQuestion2)
                }
            }

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
            } else if (editAnswer1.text!!.isEmpty()) {
                showToast("Please Enter Security Answer")
                !allValid
                return
            } else if (txtQuestion1.text.toString() == getString(R.string.mandatory)) {
                showToast("Please Select Security Question")
                !allValid
                return
            } else if (txtQuestion2.text.toString() == getString(R.string.mandatory)) {
                showToast("Please Select Security Question")
                !allValid
                return
            } else if (editAnswer2.text!!.isEmpty()) {
                showToast("Please Enter Security Answer")
                !allValid
                return
            } else if (allValid) {
                saveUser()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveUser() = if (Utils.isConnected(this)) {
        try {
            var map = HashMap<String, String>()
            map.put(ApiUrls.EmailID, editEmail.text.toString())
            map.put(ApiUrls.Password, editPassword.text.toString())
            map.put(ApiUrls.CustomerUtilityId, txtUtilityName.text.toString())
            map.put(ApiUrls.ConfirmPassword, editCPassword.text.toString())
            map.put(ApiUrls.UtilityAccountNumber, editAccountNo.text.toString())
            map.put(ApiUrls.PostalCode, editServiceZipCode.text.toString())
            map.put(ApiUrls.UserId, editEmail.text.toString())
            map.put(ApiUrls.MeterNumber, editMeterNo.text.toString())
            map.put(ApiUrls.SecurityQuestionId, txtQuestion1.text.toString())
            map.put(ApiUrls.SecurityQuestionId2, txtQuestion2.text.toString())
            map.put(ApiUrls.HintAns, editAnswer1.text.toString())
            map.put(ApiUrls.HintsAns2, editAnswer2.text.toString())
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> = apiService.registerUser(map)
            call.enqueue(object : Callback<ResponseModelClasses.RegistrationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
                    response: Response<ResponseModelClasses.RegistrationResponse>
                ) {
                    AppLog.printLog("Params " + map.toString() + "\n" + "URL " + apiService.toString() + "\n" + "Response " + response.body().toString())
                    if (response.body() != null) {
                        Log.d("send RegID===", response.body()!!.Message)

                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.RegistrationResponse>, t: Throwable) {
                    // Log error here since request failed
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

    private fun getSecurityQues(dialogOpen: Boolean = false, txtview: TextView) = if (Utils.isConnected(this)) {
        showDialog()
        try {

            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getSecurityQuestion(ApiUrls.getJSONRequestBody(ApiUrls.getBodyMap()))
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.SecurityQuestionResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>,
                    response: Response<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>
                ) {
                    dismissDialog()
                    if (response.message() != null)
                        AppLog.printLog("Response- ", response.message().toString())
                    if (response.body() != null) {
                        SecurityQuestionData.clearArrayList()
                        SecurityQuestionData.addArrayList(response.body()!!)
                        if (dialogOpen) {
                            openDialog(txtview)
                        }
                    }
                }


                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>,
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

    private fun getUtilityList(dialogOpen: Boolean = false, txtview: TextView) = if (Utils.isConnected(this)) {
        showDialog()
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
                            AppLog.printLog("UtilityList Response- ", response.body().toString())
                        if (response.message() != null) {
                            AppLog.printLog("UtilityList Response- ", response.message().toString())
                        }
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
            dismissDialog()
        }
    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

    private fun openDialog(textView: TextView) {
        try {
            val dialog = Dialog(this@SignupActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = "Select Security Question"

            val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog.dialogRecycleView.layoutManager = layoutManager
            val mAdapter = QuestionListAdapter() { position ->
                val data = SecurityQuestionData.getArrayItem(position)
                textView.text = data.Question
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}