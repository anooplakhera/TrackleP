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
            getUserProfile()
            //getSecurityQues(false, txtQuestion1)

        } catch (e: Exception) {
            e.printStackTrace()
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
            val call = apiService.getAccount(AppPrefences.getLoginUserInfo(this)!!.AccountNumber)
            call.enqueue(object : Callback<ResponseModelClasses.MyProfileResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.MyProfileResponse>,
                    response: Response<ResponseModelClasses.MyProfileResponse>
                ) {
                    try {
                        dismissDialog()
                        AppLog.printLog("User Profile Response: " + Gson().toJson(response.body()))
                        if (response.body() != null) {

                        }
                    } catch (e: Exception) {
                        dismissDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.MyProfileResponse>,
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
        dismissDialog()
        showToast(getString(R.string.internet))
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
}
