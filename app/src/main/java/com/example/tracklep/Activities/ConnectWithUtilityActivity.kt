package com.example.tracklep.Activities

import android.os.Bundle
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.R
import kotlinx.android.synthetic.main.custom_action_bar.*

class ConnectWithUtilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        try {
            txtCABtitle.text = getString(R.string.connect_with_utility)
            imgCABback.setOnClickListener {
                finish()
            }

            //getUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private fun getUserProfile() = if (Utils.isConnected(this)) {
//        showDialog()
//        try {
//            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
//            val call = apiService.getAccount(AppPrefences.getLoginUserInfo(this)!!.AccountNumber)
//            call.enqueue(object : Callback<ResponseModelClasses.MyProfileResponse> {
//                override fun onResponse(
//                    call: Call<ResponseModelClasses.MyProfileResponse>,
//                    response: Response<ResponseModelClasses.MyProfileResponse>
//                ) {
//                    try {
//                        dismissDialog()
//                        if (response.body() != null) {
//                            AppLog.printLog("User Profile Response: ", response.body().toString())
//
//                        }
//                    } catch (e: Exception) {
//                        dismissDialog()
//                        e.printStackTrace()
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ResponseModelClasses.MyProfileResponse>,
//                    t: Throwable
//                ) {
//                    dismissDialog()
//                }
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//            dismissDialog()
//        }
//    } else {
//        dismissDialog()
//        showToast(getString(R.string.internet))
//    }

}
