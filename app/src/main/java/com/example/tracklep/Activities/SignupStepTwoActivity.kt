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
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_signupsteptwo.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupStepTwoActivity : BaseActivity() {

    private var utilityID = ""
    private var EmailID = ""
    private var AccountNumber = ""
    private var MeterNumber = ""
    private var ServiceZipCode = ""
    private var Password = ""
    private var sQuesID1 = ""
    private var sQuesID2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupsteptwo)

        try {
            txtCABtitle.text = getString(R.string.registration_title_new)
            imgCABback.setOnClickListener {
                finish()
            }

            EmailID = intent.getStringExtra(ApiUrls.EmailID)
            AccountNumber = intent.getStringExtra(ApiUrls.AccountNumber)
            MeterNumber = intent.getStringExtra(ApiUrls.MeterNumber)
            ServiceZipCode = intent.getStringExtra(ApiUrls.ServiceZipCode)
            Password = intent.getStringExtra(ApiUrls.Password)
            getSecurityQues(false, txtQues2)

            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            r_lyt_ques1.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQues1)
                } else {
                    getSecurityQues(true, txtQues1)

                }
            }

            r_lyt_ques2.setOnClickListener {
                if (SecurityQuestionData.getCount() > 0) {
                    openDialog(txtQues2)
                } else {
                    getSecurityQues(true, txtQues2)
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
            if (editAnswer1.text!!.isEmpty()) {
                showToast("Please Enter Security Answer")
                !allValid
                return
            } else if (txtQues1.text.toString() == getString(R.string.security_ques1)) {
                showToast("Please Select Security Question")
                !allValid
                return
            } else if (txtQues2.text.toString() == getString(R.string.security_ques2)) {
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
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.registerUser(
                    RequestClass.getSignupRequestModel(
                        EmailID,
                        Password,
                        utilityID,
                        AccountNumber,
                        ServiceZipCode,
                        MeterNumber,
                        editAnswer1.text.toString(),
                        editAnswer2.text.toString(),
                        sQuesID1,
                        sQuesID2,
                        Password
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.RegistrationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
                    response: Response<ResponseModelClasses.RegistrationResponse>
                ) {
                    dismissDialog()
//                    AppLog.printLog("Params " + map.toString() + "\n" + "URL " + apiService.toString() + "\n" + "Response " + response.body().toString())
                    if (response.body() != null) {
                        Log.d("send RegID===", response.body()!!.Message)
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.RegistrationResponse>, t: Throwable) {
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
                        SecurityQuestionData.saveItemInHashMap()

                        if (dialogOpen) {
                            openDialog(txtview)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>, t: Throwable
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
        showToast(getString(R.string.internet))
    }

    private fun openDialog(textView: TextView) {
        try {
            val dialog = Dialog(this@SignupStepTwoActivity)
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
                when (textView) {
                    txtQues1 -> sQuesID1 = data.SecurityQuestionId
                    txtQues2 -> sQuesID2 = data.SecurityQuestionId
                }
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}