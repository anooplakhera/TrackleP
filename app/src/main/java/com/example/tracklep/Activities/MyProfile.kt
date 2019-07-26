package com.example.tracklep.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.Adapter.QuestionListAdapter
import com.example.tracklep.Adapter.SwipeItemAdapter
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.SecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.SwipeToDeleteCallback
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfile : BaseActivity() {

    private var custID = ""
    private var sQuesID1 = ""
    private var sQuesID2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        try {
            txtCABtitle.text = getString(R.string.my_account)
            imgCABadd.visibility = View.VISIBLE
            imgCABback.setOnClickListener {
                finish()
            }
            imgCABadd.setOnClickListener {
                openDialogList()
            }
            rytCommunicationAddress.setOnClickListener {
                openDialogList()
            }
            getSecurityQues(false, txtQues1)

            clickPerform()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clickPerform() {
        r_lyt_quesP1.setOnClickListener {
            if (SecurityQuestionData.getCount() > 0) {
                openDialog(txtQuestion1)
            } else {
                getSecurityQues(true, txtQuestion1)

            }
        }

        r_lyt_quesP2.setOnClickListener {
            if (SecurityQuestionData.getCount() > 0) {
                openDialog(txtQuestion2)
            } else {
                getSecurityQues(true, txtQuestion2)
            }
        }

        btnSaveProfile.setOnClickListener {
            validationField()
        }
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
                        getUserProfile()
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

    private fun getUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getAccount(
                getHeader(),
                AppPrefences.getLoginUserInfo(this)!!.AccountNumber,
                ApiUrls.getJSONRequestBody(ApiUrls.getBodyMap())
            )
            call.enqueue(object : Callback<List<ResponseModelClasses.MyProfile>> {
                override fun onResponse(
                    call: Call<List<ResponseModelClasses.MyProfile>>,
                    response: Response<List<ResponseModelClasses.MyProfile>>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            updateViews(response.body()!!.get(0))
                            AppLog.printLog("UserProfileResponse: " + Gson().toJson(response.body()));
                        }
                    } catch (e: Exception) {
                        dismissDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<List<ResponseModelClasses.MyProfile>>,
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
    }

    private fun getUpdateUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getUpdateAccount(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getUpdateAccountRequestModel(
                        editEmail.text.toString(),
                        editHomeNum.text.toString(),
                        editMobileNo.text.toString(),
                        custID,
                        AppPrefences.getLoginUserInfo(this).AccountNumber,
                        editAnswer1.text.toString(),
                        editAnswer2.text.toString(),
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
                            AppLog.printLog("UserUpdateProfileResponse: " + Gson().toJson(response.body()));
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
    }

    fun updateViews(data: ResponseModelClasses.MyProfile) {
        custID = data.CustomerId.toString()
        sQuesID1 = data.SecurityQuestionId.toString()
        sQuesID2 = data.SecurityQuestionId2.toString()
        txtUserName.text = data.FullName
        editEmail.setText(data.EmailId)
        editHomeNum.setText(data.HomePhone)
        editMobileNo.setText(data.MobilePhone)
        editAnswer1.setText(data.HintsAns)
        editAnswer2.setText(data.HintsAns2)
        txtCommunicationAdd.text = data.CommunicationAddress
        txtCommunicationAdd.setTextColor(resources.getColor(R.color.colorBlack))
        txtQuestion1.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId.toString())
        txtQuestion2.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId2.toString())
        txtAccNumber.text = "Account Number : " + data.UtilityAccountNumber
    }

    private fun openDialog(textView: TextView) {
        try {
            val dialog = Dialog(this@MyProfile)
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
                    txtQuestion1 -> sQuesID1 = data.SecurityQuestionId
                    txtQuestion2 -> sQuesID2 = data.SecurityQuestionId
                }
                textView.setTextColor(resources.getColor(R.color.colorBlack))
                dialog.dismiss()
            }
            dialog.dialogRecycleView.adapter = mAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openDialogList() {
        try {
            val dialog = Dialog(this@MyProfile)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
            dialog.txtTitleTop.text = title

            val mSwipeItemAdapter = SwipeItemAdapter((1..5).map { "Item: $it" }.toMutableList()) { position ->
                dialog.dismiss()
            }


            dialog.dialogRecycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            dialog.dialogRecycleView.layoutManager = LinearLayoutManager(this)
            dialog.dialogRecycleView.adapter = mSwipeItemAdapter

            val swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    var alertDialog = AlertDialog.Builder(this@MyProfile)
                    alertDialog.setTitle(getString(R.string.app_name))
                    alertDialog.setMessage("Are you sure you want to delete communication address?")
                    alertDialog.setNeutralButton("Cancel") { _, _ ->

                    }

                    alertDialog.setPositiveButton("Yes") { dialog, which ->
                        dialog.dismiss()
                        val adapter = dialogRecycleView.adapter as SwipeItemAdapter
                        adapter.removeAt(viewHolder.adapterPosition)
                        if (adapter.isEmpty()) {
                            dialog.dismiss()
                        }
                    }

                    alertDialog.show()

                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(dialog.dialogRecycleView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun validationField() {
        try {
            var allValid = true
            if (editEmail.text!!.isEmpty()) {
                showToast("Please Enter Email")
                !allValid
                return
            } else if (editHomeNum.text!!.isEmpty()) {
                showToast("Please Enter Home Phone Number")
                !allValid
                return
            } else if (editMobileNo.text!!.isEmpty()) {
                showToast("Please Enter Mobile Number")
                !allValid
                return
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString())) {
                showToast("Please Enter Valid Email")
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
                getUpdateUserProfile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
