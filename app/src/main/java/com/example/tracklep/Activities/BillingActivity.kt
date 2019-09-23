package com.example.tracklep.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_utility_bill.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToInt

class BillingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility_bill)

        getBillingDetails()
        try {
            txtCABtitle.text = getString(R.string.billing)
            txtCArightTop.visibility = View.GONE
            txtCArightTop.text = "View Bill"
            imgCABback.setOnClickListener {
                finish()
            }

            btnPayNow.setOnClickListener {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(getString(R.string.app_name))
                alertDialog.setMessage("You will be redirected to EOCWD online payment system. Click Proceed to continue.")
                alertDialog.setNeutralButton("Cancel") { _, _ ->
                }

                alertDialog.setPositiveButton("Proceed") { dialog, which ->
                    dialog.dismiss()
                    startWebActivity("Pay Bill", "https://eocwd.epayub.com")
                }

                alertDialog.show()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startWebActivity(title: String, url: String) {
        try {
            intent = Intent(this@BillingActivity, WebViewActivity::class.java)
            intent.putExtra("contentTitle", title)
            intent.putExtra("contentUrl", "https://eocwd.epayub.com")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBillingDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.BillingDashboardResponse> =
                apiService.getBillingDashboard(
                    getHeader(),
                    ApiUrls.getJSONRequestBody(
                        RequestClass.getBillingDetailsRequestModel(
                            AppPrefences.getAccountNumber(this), AppPrefences.getDataBaseInfo(this)!!
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.BillingDashboardResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.BillingDashboardResponse>,
                    response: Response<ResponseModelClasses.BillingDashboardResponse>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("getBillingDetails: " + Gson().toJson(response.body()));
                            updateViews(response.body()!!.Results.Table)
                        }
                    } catch (e: Exception) {
                        showSuccessPopup("Error. Try again later.")
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.BillingDashboardResponse>,
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

    fun updateViews(data: List<ResponseModelClasses.BillingDashboardResponse.Results1.TableOne>) {

        try {
            for (i in 0 until data.size) {
                val attr = data[i].HeaderName

                if (attr.equals("Capital Project Charge"))
                    txtCapitalProjectChargeValue.text = "$" + data[i].Value.toString()

                if (attr.equals("Current Meter Reading"))
                    txtCurrentMeterReadingValue.text = data[i].Value.toString()

                if (attr.equals("Meter Number"))
                    txtMeterNumberValue.text = data[i].Value.toString()

                if (attr.equals("Previous Meter Reading"))
                    txtPreviousMeterReadingValue.text = data[i].Value.toString()

                if (attr.equals("Meter Service charge"))
                    txtMeterServiceChargValue.text = "$" + data[i].Value.toString()

                if (attr.equals("Total Water Usage"))
                    txtTotalWaterUsageValue.text = data[i].Value.toString()

                if (attr.equals("Consumption Charge"))
                    txtConsumptionChargesValue.text = "$" + data[i].Value.toString()

                if (attr.equals("Billing Period"))
                    txtBillingPeriodFromValue.text = data[i].Value.toString()

                if (attr.equals("Account Balance"))
                    txtAccountBalanceValue.text = "$" + data[i].Value.toString()

                if (attr.equals("Account Total"))
                    txtAccountTotalValue.text = "$" + data[i].Value.toString()

                if (attr.equals("Account Total"))
                    txtTotalDueMain.text = "$" + data[i].Value.toString()



                if (attr.equals("Due Date")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
                        val formatter2 = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
                        val date = LocalDate.parse(data[i].Value.toString(), formatter)
                        val currentDate = LocalDate.now()
                        val dueDays = ChronoUnit.DAYS.between(currentDate, date)
                        val format_date = date.format(formatter2)
                        AppLog.printLog("Updated Date: $date")
                        AppLog.printLog("Formatted Date: $format_date")
                        AppLog.printLog("Days Due: $dueDays")
                        txtPaymentDueDateMain.text = "Due Date: $format_date"

                        if (dueDays.toInt() < 0)
                            txtMeterValue.text = (dueDays.toInt() * -1).toString() + " days due"
                        else
                            txtMeterValue.text = "$dueDays days left"

                        arc_progress.progress =
                            Utils.getProgressValue(30.0, 12.0)
                                .roundToInt()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                }
                /*if (attr.equals("Previous Meter Reading"))
                    if()
                    txtPreviousMeterReadingValue.text = data[i].Value.toString()*/


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        txtCArightTop.visibility = View.GONE
    }

}
