package com.example.tracklep.Activities

import android.app.Dialog
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
import com.example.tracklep.Adapter.ConnectMeListAdapter
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.ConnectMeData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnectWithUtilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        try {
            txtCABtitle.text = getString(R.string.connect_with_utility)
            imgCABback.setOnClickListener {
                finish()
            }

            getConnectMeDetails(false, txtTopicName)

            txtTopicName.setOnClickListener {
                if (ConnectMeData.getCount() > 0) {
                    openListDialog("Select Topic Name", txtTopicName)
                } else {
                    getConnectMeDetails(true, txtTopicName)
                }
            }

            btnConnectMeSubmit.setOnClickListener {
                validationField()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getConnectMeDetails(dialogOpen: Boolean = false, textView: TextView) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.ConnectWithUtilityResponse> = apiService.getConnectWithUtility(
                getHeader(),
                AppPrefences.getAccountNumber(this),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getConnectWithUtilityRequestModel()
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.ConnectWithUtilityResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.ConnectWithUtilityResponse>,
                    response: Response<ResponseModelClasses.ConnectWithUtilityResponse>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            ConnectMeData.clearArrayList()
                            ConnectMeData.addArrayList(response.body()!!.Results.Table1)
                            AppLog.printLog("getConnectMeDetails: " + Gson().toJson(response.body()));
                            updateViews(response.body()!!.Results.Table3)
                            if (dialogOpen) {
                                openListDialog("Select Topic Name", textView)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.ConnectWithUtilityResponse>, t: Throwable) {
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

    fun updateViews(data: List<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableThree>) {

        txtContactUsEmailValue.setText(data.get(0).CustomerServiceEmail)
        txtContactUsPhoneValue.setText(data.get(0).PrimaryPhone)
        txtAddressValue.setText(data.get(0).utilityAddress)
        txtCustomerServiceHourValue.setText(data.get(0).UtilityTime)

    }

    private fun validationField() {
        try {
            var allValid = true
            if (txtTopicName.text.toString() == getString(R.string.select_topic)) {
                showToast("Please Select Topic")
                !allValid
                return
            } else if (editSubjectValue.text!!.isEmpty()) {
                showToast("Please Enter Subject")
                !allValid
                return
            } else if (editPostalCode.text!!.isEmpty()) {
                showToast("Please Enter Postal Code")
                !allValid
                return
            } else if (allValid) {
                //getUpdateUserProfile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openListDialog(title: String, textView: TextView) = try {
        val dialog = Dialog(this@ConnectWithUtilityActivity)
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
        val mAdapter = ConnectMeListAdapter() { position ->
            val data = ConnectMeData.getArrayItem(position)
            textView.text = data.TopicName
            textView.setTextColor(resources.getColor(R.color.colorBlack))
            dialog.dismiss()
        }
        dialog.dialogRecycleView.adapter = mAdapter
    } catch (e: Exception) {
        e.printStackTrace()
    }

   /* private fun getUpdateUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getUpdateAccount(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getUpdateAccountRequestModel(
                        editEmail.text.toString(),
                        editHomePhoneNumberValue.text.toString(),
                        editMobileNumberValue.text.toString(),
                        custID,
                        AppPrefences.getAccountNumber(this),
                        editAns1Value.text.toString(),
                        editAns2Value.text.toString(),
                        sQuesID1,
                        sQuesID2
                    )
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.UpdateProfile> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.UpdateProfile>,
                    response: Response<ResponseModelClasses.UpdateProfile>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            showToast(response.body()!!.Message)
                            getUserProfile()
                            AppLog.printLog("UserUpdateProfileResponse: " + Gson().toJson(response.body()))
                        }
                    } catch (e: Exception) {
                        dismissDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.UpdateProfile>,
                    t: Throwable
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
    }*/
}