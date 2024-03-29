package com.aquatrax.tracklep.Activities

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.Adapter.ConnectMeListAdapter
import com.aquatrax.tracklep.ApiClient.ApiClient
import com.aquatrax.tracklep.ApiClient.ApiInterface
import com.aquatrax.tracklep.ApiClient.ApiUrls
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.DataClasses.ConnectMeData
import com.aquatrax.tracklep.DataModels.ResponseModelClasses
import com.aquatrax.tracklep.R
import com.aquatrax.tracklep.Utils.AppConstants
import com.aquatrax.tracklep.Utils.AppLog
import com.aquatrax.tracklep.Utils.RequestClass
import com.aquatrax.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.aquatrax.tracklep.DataClasses.UtilitiesData


class ConnectWithUtilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        try {
            txtCABtitle.text = getString(R.string.connect_with_utility)
            imgCABback.setOnClickListener {
                finish()
            }

            txtContactUsEmailValue.setText("admin@eocwd.com")
            txtContactUsPhoneValue.setText("(714) 538-5815")
            txtEmergencyNumberValue.setText("(714) 538-5815")
            txtAddressValue.setText("185 N McPherson Rd, Orange, CA 92869, USA")
            txtCustomerServiceHourValue.setText("8:00 AM to 5:00 PM")

            txtContactUsPhoneValue.setOnClickListener {
                try {
                    val intent =
                        Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtContactUsPhoneValue.text))
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@setOnClickListener;
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            txtEmergencyNumberValue.setOnClickListener {
                try {
                    val intent =
                        Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtEmergencyNumberValue.text))
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@setOnClickListener;
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            txtContactUsEmailValue.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:") // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, txtContactUsEmailValue.text)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Connect With Agency")
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            txtTopicName.setOnClickListener {
                if (ConnectMeData.getCount() > 0) {
                    openListDialog("Select Report Type", txtTopicName)
                } else {
                    getConnectMeDetails(true, txtTopicName)
                }
            }

            btnConnectMeSubmit.setOnClickListener {
                validationField()
            }

            getConnectMeDetails(false, txtTopicName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getConnectMeDetails(dialogOpen: Boolean = false, textView: TextView) =
        if (Utils.isConnected(this)) {
            showDialog()
            try {
                val apiService =
                    ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)

                val call: Call<ResponseModelClasses.ConnectWithUtilityResponse> =
                    apiService.getConnectWithUtility(
                        getHeader(),
                        AppPrefences.getAccountNumber(this),
                        ApiUrls.getJSONRequestBody(
                            RequestClass.getConnectWithUtilityRequestModel(
                                AppPrefences.getDataBaseInfo(
                                    this
                                )!!
                            )
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
                                updateViews(/*response.body()!!.Results.Table3, */response.body()!!.Results.Table)

                                if (dialogOpen) {
                                    openListDialog("Select Report Type", textView)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseModelClasses.ConnectWithUtilityResponse>,
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

    fun updateViews(
        /*data: List<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableThree>,*/
        table1: ArrayList<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableOne>
    ) {

        try {
            //if (data.get(0).CustomerServiceEmail == null || data.get(0).CustomerServiceEmail.equals("")) {
            txtContactUsEmailValue.setText("admin@eocwd.com")
            txtContactUsPhoneValue.setText("(714) 538-5815")
            txtEmergencyNumberValue.setText("(714) 538-5815")
            txtAddressValue.setText("185 N McPherson Rd, Orange, CA 92869, USA")
            txtCustomerServiceHourValue.setText("8:00 AM to 5:00 PM")
            /* } else {
                 txtContactUsEmailValue.setText(data.get(0).CustomerServiceEmail)
                 txtContactUsPhoneValue.setText(data.get(0).PrimaryPhone)
                 txtAddressValue.setText(data.get(0).utilityAddress)
                 txtCustomerServiceHourValue.setText(data.get(0).UtilityTime)
             }*/

            editCustomerName.setText(table1.get(0).customername)
            editServiceAccNum.setText(table1.get(0).utilityaccountnumber)
            editCustomerEmail.setText(table1.get(0).emailid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun validationField() {
        try {
            var allValid = true
            if (txtTopicName.text.toString() == getString(R.string.select_topic)) {
                showSuccessPopup("Please Select Report Type")
                !allValid
                return
            } else if (editSubjectValue.text!!.isEmpty()) {
                showSuccessPopup("Please enter Subject")
                !allValid
                return
            } else if (editCustomerName.text!!.isEmpty()) {
                showSuccessPopup("Please enter Customer Name")
                !allValid
                return
            } else if (editServiceAccNum.text!!.isEmpty()) {
                showSuccessPopup("Please enter Service Account Number")
                !allValid
                return
            } else if (editCustomerEmail.text!!.isEmpty()) {
                showSuccessPopup("Please enter Postal Code")
                !allValid
                return
            } else if (editSubjectValue.text!!.isEmpty()) {
                showSuccessPopup("Please select Subject")
                !allValid
                return
            } else if (editMessageValue.text!!.isEmpty()) {
                showSuccessPopup("Please enter Message")
                !allValid
                return
            } else if (allValid) {
                setConnectMe()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun openListDialog(title: String, textView: TextView) = try {
        val dialog = Dialog(this@ConnectWithUtilityActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
        dialog.txtTitleTop.text = title

        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        dialog.dialogRecycleView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        val itemDecor = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
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

    private fun setConnectMe() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.setConnectMe(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getConnectMeRequestModel(
                        editCustomerEmail.text.toString(),
                        AppConstants.SelectedTopicID,
                        editSubjectValue.text.toString(),
                        editMessageValue.text.toString(),
                        AppPrefences.getAccountNumber(this),
                        AppPrefences.getDataBaseInfo(this)!!
                    )
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.SetConnectMeResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.SetConnectMeResponseModel>,
                    response: Response<ResponseModelClasses.SetConnectMeResponseModel>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("SetConnectMeResponse: " + Gson().toJson(response.body()))
                            if (response.body()!!.Status == "1") {
                                showSuccessPopup("Thank you for contacting us. We have received your request and will get back as soon as possible.")
                                AppConstants.SelectedTopicID = ""
                                txtTopicName.text = getString(R.string.select_topic)
                            } else
                                showSuccessPopup(response.body()!!.Message)

                            editSubjectValue.setText("")
                            editMessageValue.setText("")
                        } else
                            AppLog.printLog("SetConnectMeResponse: null")
                    } catch (e: Exception) {
                        dismissDialog()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.SetConnectMeResponseModel>,
                    t: Throwable
                ) {
                    dismissDialog()
                    t.printStackTrace()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        showToast(getString(R.string.internet))
    }
}