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
import android.telephony.PhoneNumberFormattingTextWatcher
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
import com.example.tracklep.Utils.*
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
    private var deleteCommAddAaccountNumber = ""
    private var positionu = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        try {
            txtCABtitle.text = getString(R.string.my_account)
            imgCABadd.visibility = View.VISIBLE
            imgCABback.setOnClickListener {
                finish()
            }


            //PhoneNumberUtils.formatNumber(editHomePhoneNumberValue, int defaultFormattingType);

            if (SecurityQuestionData.getCount() > 0) {
                getUserProfile()
            } else {
                getSecurityQues(false, txtQues1Value)
            }

            clickPerform()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppConstants.IsAccountAdded) {
            AppConstants.IsAccountAdded = false
            getUserProfile()
        }

    }

    private fun clickPerform() {

        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSecurityQues(dialogOpen: Boolean = false, txtview: TextView) =
        if (Utils.isConnected(this)) {
            showDialog()
            try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
                val call = apiService.getSecurityQuestion(
                    ApiUrls.getJSONRequestBody(
                        ApiUrls.getBodyMap(AppPrefences.getDataBaseInfo(this)!!)
                    )
                )
                call.enqueue(object :
                    Callback<ArrayList<ResponseModelClasses.SecurityQuestionResponse>> {
                    override fun onResponse(
                        call: Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>,
                        response: Response<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>
                    ) {
                        try {
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
                        } catch (e: Exception) {
                            e.printStackTrace()
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
            showToast(getString(R.string.internet))
        }

    private fun getUserProfile() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getAccount(
                getHeader(),
                AppPrefences.getAccountNumber(this),
                ApiUrls.getJSONRequestBody(ApiUrls.getBodyMap(AppPrefences.getDataBaseInfo(this)!!))
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
            var billingPrefrence = "0";
            if (rb_paperless_bill.isChecked)
                billingPrefrence = "1";
            else if (rb_paper_bill.isChecked)
                billingPrefrence = "2";
            else if (rb_both_bill.isChecked)
                billingPrefrence = "2";

            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getUpdateAccount(
                getHeader(),

                ApiUrls.getJSONRequestBody(
                    RequestClass.getUpdateAccountRequestModel(
                        editEmail.text.toString(),
                        editHomePhoneNumberValue.text.toString(),
                        editMobilePhoneValue.text.toString(),
                        editBusinessPhoneNumberValue.text.toString(),
                        billingPrefrence,
                        custID,
                        AppPrefences.getAccountNumber(this),
                        editAns1Value.text.toString(),
                        editAns2Value.text.toString(),
                        sQuesID1,
                        sQuesID2, AppPrefences.getDataBaseInfo(this)!!
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
                            showSuccessPopup(response.body()!!.Message)
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
        try {
            custID = data.CustomerId.toString()
            sQuesID1 = data.SecurityQuestionId.toString()
            sQuesID2 = data.SecurityQuestionId2.toString()
            txtUserName.text = data.FullName
            editEmail.setText(data.EmailId)
            editHomePhoneNumberValue.setText(data.HomePhone)
            editMobilePhoneValue.setText(data.MobilePhone)
            editBusinessPhoneNumberValue.setText(data.BusinessHomePhone)
            editHomePhoneNumberValue.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
            editMobilePhoneValue.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
            editBusinessPhoneNumberValue.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
            editAns1Value.setText(data.HintsAns)
            editAns2Value.setText(data.HintsAns2)
            txtQues1Value.text =
                SecurityQuestionData.getQuestionName(data.SecurityQuestionId.toString())
            txtQues2Value.text =
                SecurityQuestionData.getQuestionName(data.SecurityQuestionId2.toString())
            txtAccNumber.text = "Account Number : " + data.UtilityAccountNumber
            txtCommunicationAddressValue.text = data.CommunicationAddress
            txtCommunicationAddressValue.setTextColor(resources.getColor(R.color.colorBlack))

            if (data.BillingPreference.equals("1")) {
                rb_paperless_bill.isChecked = true
                rb_paper_bill.isChecked = false
                rb_both_bill.isChecked = false
            } else if (data.BillingPreference.equals("2")) {
                rb_paperless_bill.isChecked = false
                rb_paper_bill.isChecked = true
                rb_both_bill.isChecked = false
            } else if (data.BillingPreference.equals("3")) {
                rb_paperless_bill.isChecked = false
                rb_paper_bill.isChecked = false
                rb_both_bill.isChecked = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openDialog(textView: TextView) {
        try {
            val dialog = Dialog(this@MyProfile)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
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
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.setTitle("Communication Address")
            dialog.show()
            dialog.txtTitleTop.text = title
            dialog.txtTitleTop.textSize = 13f

            val mSwipeItemAdapter =
                SwipeItemAdapter(ProfileListData.getArrayAddress()) { position ->
                    updateViews(ProfileListData.getArrayItem(position))
                    AppPrefences.setProfileInfo(
                        this@MyProfile,
                        ProfileListData.getArrayItem(position)
                    )
                    /*deleteCommAddAaccountNumber =
                        ProfileListData.getArrayItem(position).AccountNumber.toString()
                    positionu = position.toString()*/
                    AppPrefences.setAccountNumber(
                        this@MyProfile,
                        ProfileListData.getArrayItem(position).AccountNumber.toString()
                    )
                    dialog.dismiss()
                }


            dialog.dialogRecycleView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            dialog.dialogRecycleView.layoutManager = LinearLayoutManager(this)
            dialog.dialogRecycleView.adapter = mSwipeItemAdapter

            var swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    try {
                        positionu = viewHolder.adapterPosition.toString()
                        deleteCommAddAaccountNumber =
                            ProfileListData.getArrayItem(positionu.toInt()).AccountNumber.toString()
                        var alertDialog = AlertDialog.Builder(this@MyProfile)
                        alertDialog.setTitle(getString(R.string.app_name))
                        alertDialog.setMessage("Are you sure you want to delete communication address?")
                        alertDialog.setNeutralButton("Cancel") { _, _ ->
                            if (mSwipeItemAdapter != null) {
                                mSwipeItemAdapter.notifyDataSetChanged()
                            }
                        }

                        alertDialog.setPositiveButton("Yes") { dialog, which ->
                            if (setDeleteAccount()) {
                                mSwipeItemAdapter.removeAt(viewHolder.adapterPosition)
                                mSwipeItemAdapter.notifyDataSetChanged()
                                if (mSwipeItemAdapter.isEmpty()) {
                                    dialog.dismiss()
                                }
                            } else {
                                mSwipeItemAdapter.notifyDataSetChanged()
                                if (mSwipeItemAdapter.isEmpty()) {
                                    dialog.dismiss()
                                }
                            }

                        }

                        alertDialog.show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

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
                showSuccessPopup(getString(R.string.error_message_enter_email))
                !allValid
                return
            } else if (!editEmail.text!!.isEmpty() && !Utils.isValidEmail(editEmail.text.toString())) {
                showSuccessPopup("Please enter valid Email")
                !allValid
                return
            } else if (editHomePhoneNumberValue.text!!.isEmpty()) {
                showSuccessPopup("Please enter Home Phone Number")
                !allValid
                return
            } else if (!editHomePhoneNumberValue.text!!.isEmpty() && editHomePhoneNumberValue.text!!.length < 10) {
                showSuccessPopup("Please enter 10 digits Home Phone Number")
                !allValid
                return
            } else if (editMobilePhoneValue.text!!.isEmpty()) {
                showSuccessPopup("Please enter Mobile Number")
                !allValid
                return
            } else if (!editMobilePhoneValue.text!!.isEmpty() && editMobilePhoneValue.text!!.length < 10) {
                showSuccessPopup("Please enter 10 digits Mobile Number")
                !allValid
                return
            } else if (editAns1Value.text!!.isEmpty()) {
                showSuccessPopup("Please enter Security Answer")
                !allValid
                return
            } else if (txtQues1Value.text.toString() == getString(R.string.mandatory)) {
                showSuccessPopup("Please select Security Question")
                !allValid
                return
            } else if (txtQues2Value.text.toString() == getString(R.string.mandatory)) {
                showSuccessPopup("Please select Security Question")
                !allValid
                return
            } else if (editAns2Value.text!!.isEmpty()) {
                showSuccessPopup("Please enter Security Answer")
                !allValid
                return
            } else if (allValid) {
                getUpdateUserProfile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDeleteAccount(): Boolean {
        var isSuccess = false
        if (Utils.isConnected(this)) {
            showDialog()
            try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
                val call: Call<ResponseModelClasses.SetDeleteAccountResponseModel> =
                    apiService.getDeleteAccount(
                        getHeader(),
                        ApiUrls.getJSONRequestBody(
                            RequestClass.getDeleteAccountRequestModel(
                                deleteCommAddAaccountNumber, AppPrefences.getDataBaseInfo(this)!!
                            )
                        ),
                        deleteCommAddAaccountNumber

                    )//AppPrefences.getAccountNumber(this)
                call.enqueue(object : Callback<ResponseModelClasses.SetDeleteAccountResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModelClasses.SetDeleteAccountResponseModel>,
                        response: Response<ResponseModelClasses.SetDeleteAccountResponseModel>
                    ) {
                        try {
                            dismissDialog()
                            if (response.body() != null) {

                                AppLog.printLog(
                                    "setDeleteAccountResponse: " + Gson().toJson(
                                        response.body()
                                    )
                                );
                                showSuccessPopup(response.body()!!.Message)
                                isSuccess = !response.body()!!.Status.equals("0")
                                if (response.body()!!.Status.equals("1"))
                                    isSuccess = true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseModelClasses.SetDeleteAccountResponseModel>,
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
            //dismissDialog()
            showToast(getString(R.string.internet))
        }
        return isSuccess
    }
}
