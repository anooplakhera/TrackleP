package com.example.tracklep.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
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
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.example.tracklep.DataClasses.UtilitiesData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forgot_password_first.*
import kotlinx.android.synthetic.main.activity_forgot_password_first.textUtilities
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordFirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_first)

        try {
            txtCABtitle.text = getString(R.string.forgot_password)
            imgCABback.setOnClickListener {
                finish()
            }

            btnSubmitStep1.setOnClickListener {

                if (textUtilities.text.toString() == getString(R.string.select_utility)) {
                    showSuccessPopup("Please select Water District/ Agency")
                } else if (editEmailF1.text!!.isNotEmpty() && Utils.isValidEmail(editEmailF1.text.toString()))
                    getUserEmail()
                else
                    showSuccessPopup("Please enter valid Email")
            }

            textUtilities.setOnClickListener {
                if (UtilitiesData.getCount() > 0) {
                    openDialog(getString(R.string.select_utility), textUtilities)
                } else {
                    getUtilityList(true, textUtilities)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getUserEmail() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getResetUserPass1(RequestClass.getForgetRequestStepOne(editEmailF1.text.toString(),
                AppPrefences.getDataBaseInfo(this)!!))
            call.enqueue(object : Callback<ResponseModelClasses.ResetPassStep1Response> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ResetPassStep1Response>,
                    response: Response<ResponseModelClasses.ResetPassStep1Response>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            if (response.body()!!.Table[0].Status != null && response.body()!!.Table[0].Status == "0") {
                                showSuccessPopup(response.body()!!.Table[0].Message)
                            } else {
                                AppLog.printLog("ForgetStep1reponse ", Gson().toJson(response.body()!!))
                                ResetPassSecurityQuestionData.clearArrayList()
                                ResetPassSecurityQuestionData.addArrayList(response.body()!!.Table)
                                val intent =
                                    Intent(this@ForgotPasswordFirstActivity, ForgotPasswordSecondActivity::class.java)
                                intent.putExtra(ApiUrls.EmailID, editEmailF1.text.toString())
                                startActivity(intent)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
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

    private fun openDialog(title: String, textView: TextView) {
        try {
            val dialog = Dialog(this@ForgotPasswordFirstActivity)
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
                            openDialog(getString(R.string.select_utility), textView)
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
}
