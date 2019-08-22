package com.example.tracklep.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.telephony.PhoneNumberUtils
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
import com.example.tracklep.DataClasses.ProfileListData
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

            if (SecurityQuestionData.getCount() > 0) {
                getUserProfile()
            } else {
                getSecurityQues(false, txtQues1Value)
            }

            //PhoneNumberUtils.formatNumber(editHomePhoneNumberValue, int defaultFormattingType);
            clickPerform()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {

        imgCABadd.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }
        rytCommunicationAddress.setOnClickListener {
            openDialogList()
        }

        r_lyt_quesP1.setOnClickListener {
            if (SecurityQuestionData.getCount() > 0) {
                openDialog(txtQues1Value)
            } else {
                getSecurityQues(true, txtQues1Value)
            }
        }

        r_lyt_ques2.setOnClickListener {
            if (SecurityQuestionData.getCount() > 0) {
                openDialog(txtQues2Value)
            } else {
                getSecurityQues(true, txtQues2Value)
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

    private fun getUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getAccount(
                getHeader(),
                AppPrefences.getAccountNumber(this),
                ApiUrls.getJSONRequestBody(ApiUrls.getBodyMap())
            )
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.MyProfile>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.MyProfile>>,
                    response: Response<ArrayList<ResponseModelClasses.MyProfile>>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            ProfileListData.clearArrayList()
                            ProfileListData.addArrayList(response.body()!!)
                            updateViews(ProfileListData.getArrayItem(ProfileListData.getPositionDefault()))
                            AppPrefences.setProfileInfo(
                                this@MyProfile,
                                ProfileListData.getArrayItem(ProfileListData.getPositionDefault())
                            )
                            AppPrefences.setAccountNumber(
                                this@MyProfile,
                                ProfileListData.getArrayItem(ProfileListData.getPositionDefault()).AccountNumber.toString()
                            )

                            AppLog.printLog("UserProfileResponse: " + Gson().toJson(response.body()))
                        }
                    } catch (e: Exception) {
                        dismissDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.MyProfile>>,
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
    }

    fun updateViews(data: ResponseModelClasses.MyProfile) {
        custID = data.CustomerId.toString()
        sQuesID1 = data.SecurityQuestionId.toString()
        sQuesID2 = data.SecurityQuestionId2.toString()
        txtUserName.text = data.FullName
        editEmail.setText(data.EmailId)
        editHomePhoneNumberValue.setText(data.HomePhone)
        editMobileNumberValue.setText(data.MobilePhone)
        editAns1Value.setText(data.HintsAns)
        editAns2Value.setText(data.HintsAns2)
        txtQues1Value.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId.toString())
        txtQues2Value.text = SecurityQuestionData.getQuestionName(data.SecurityQuestionId2.toString())
        txtAccNumber.text = "Account Number : " + data.UtilityAccountNumber
        txtCommunicationAddressValue.text = data.CommunicationAddress
        txtCommunicationAddressValue.setTextColor(resources.getColor(R.color.colorBlack))
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
            val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            dialog.dialogRecycleView.addItemDecoration(itemDecor)
            val mAdapter = QuestionListAdapter { position ->
                val data = SecurityQuestionData.getArrayItem(position)
                textView.text = data.Question
                when (textView) {
                    txtQues1Value -> sQuesID1 = data.SecurityQuestionId
                    txtQues2Value -> sQuesID2 = data.SecurityQuestionId
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
            dialog.txtTitleTop.textSize = 13f

            val mSwipeItemAdapter = SwipeItemAdapter(ProfileListData.getArrayAddress()) { position ->
                updateViews(ProfileListData.getArrayItem(position))
                AppPrefences.setProfileInfo(this@MyProfile, ProfileListData.getArrayItem(position))
                AppPrefences.setAccountNumber(
                    this@MyProfile,
                    ProfileListData.getArrayItem(position).AccountNumber.toString()
                )
                dialog.dismiss()
            }


            dialog.dialogRecycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            dialog.dialogRecycleView.layoutManager = LinearLayoutManager(this)
            dialog.dialogRecycleView.adapter = mSwipeItemAdapter

            var swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    var alertDialog = AlertDialog.Builder(this@MyProfile)
                    alertDialog.setTitle(getString(R.string.app_name))
                    alertDialog.setMessage("Are you sure you want to delete communication address?")
                    alertDialog.setNeutralButton("Cancel") { _, _ ->
                        if (mSwipeItemAdapter != null) {
                            mSwipeItemAdapter.notifyDataSetChanged()
                        }
                    }

                    alertDialog.setPositiveButton("Yes") { dialog, which ->
                        mSwipeItemAdapter.removeAt(viewHolder.adapterPosition)
                        if (mSwipeItemAdapter.isEmpty()) {
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
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString())) {
                showToast("Please Enter Valid Email")
                !allValid
                return
            } else if (editHomePhoneNumberValue.text!!.isEmpty()) {
                showToast("Please Enter Home Phone Number")
                !allValid
                return
            } else if (!editHomePhoneNumberValue.text!!.isEmpty() && editHomePhoneNumberValue.text!!.length < 10) {
                showToast("Please Enter 10 Digits Home Phone Number")
                !allValid
                return
            } else if (editMobileNumberValue.text!!.isEmpty()) {
                showToast("Please Enter Mobile Number")
                !allValid
                return
            } else if (!editMobileNumberValue.text!!.isEmpty() && editMobileNumberValue.text!!.length < 10) {
                showToast("Please Enter 10 Digits Mobile Number")
                !allValid
                return
            } else if (editAns1Value.text!!.isEmpty()) {
                showToast("Please Enter Security Answer")
                !allValid
                return
            } else if (txtQues1Value.text.toString() == getString(R.string.mandatory)) {
                showToast("Please Select Security Question")
                !allValid
                return
            } else if (txtQues2Value.text.toString() == getString(R.string.mandatory)) {
                showToast("Please Select Security Question")
                !allValid
                return
            } else if (editAns2Value.text!!.isEmpty()) {
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
