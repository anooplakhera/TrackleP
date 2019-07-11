package com.example.tracklep.Activities

import android.os.Bundle
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.Fragments.UtilityBillFragment
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.activity_billing_dashboard.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillingDashboard : BaseActivity() {

    var BillingDashboardModel = ArrayList<ResponseModelClasses.BillingDashboardResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_dashboard)

        txtCABtitle.text = getString(R.string.billing)
        imgCABback.setOnClickListener {
            finish()
        }

        getBillingDashboard()

        lytUtilityBill.setOnClickListener {
            try {
                val fragment = UtilityBillFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.lytfragment,
                    fragment,
                    "UtilityBillFragment"
                )
                fragmentTransaction.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun getBillingDashboard() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call = apiService.getBillingDashboard(ApiUrls.getJSONRequestBody(ApiUrls.getBodyMap()))
            call.enqueue(object : Callback<ArrayList<ResponseModelClasses.BillingDashboardResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseModelClasses.BillingDashboardResponse>>,
                    response: Response<ArrayList<ResponseModelClasses.BillingDashboardResponse>>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        AppLog.printLog("Billing Dashboard Response: ", response.body().toString())

                        BillingDashboardModel = response.body()!!
                        txtUtilityDueDate.setText("Due Date " + response.body()!![0].Question)
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseModelClasses.BillingDashboardResponse>>,
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

}