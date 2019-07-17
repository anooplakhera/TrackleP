package com.example.tracklep.Activities

import android.os.Bundle
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v4.os.HandlerCompat.postDelayed
import android.os.Handler
import kotlinx.android.synthetic.main.activity_conservation.*


class ConservationActivity : BaseActivity() {

    val imageArray = intArrayOf(
        R.drawable.billing_blue,
        R.drawable.connect_blue,
        R.drawable.conserve_water
    )
    val backgroundColorArray = intArrayOf(
        R.color.colorAppGray,
        R.color.colorPrimary,
        R.color.colorPrimaryDark
    )
    val tipsArray = intArrayOf(
        R.string.conservation_tip1,
        R.string.conservation_tip2,
        R.string.conservation_tip3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conservation)

        try {
            txtCABtitle.text = getString(R.string.water_conversation)
            imgCABback.setOnClickListener {
                finish()
            }

            val handler = Handler()
            val runnable = object : Runnable {
                var i = 0

                override fun run() {

                    imgConservationTipImage.setImageResource(imageArray[i])
                    ryl_conservationParent.setBackgroundColor(backgroundColorArray[i])
                    txtConservationTip.setText(tipsArray[i])

                    i++
                    if (i > imageArray.size-1) {
                        i = 0
                    }
                    handler.postDelayed(this, 5000)
                }
            }
            handler.postDelayed(runnable, 100)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}