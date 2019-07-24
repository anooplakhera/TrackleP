package com.example.tracklep.Activities

import android.os.Bundle
import android.os.Handler
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.R
import kotlinx.android.synthetic.main.activity_conservation.*
import kotlinx.android.synthetic.main.custom_action_bar.*


class ConservationActivity : BaseActivity() {

    val imageArray = intArrayOf(
        R.drawable.conserve_water,
        R.drawable.connect_blue,
        R.drawable.conserve_water
    )
    val backgroundColorArray = intArrayOf(
        R.color.colorConservation1,
        R.color.colorConservation2,
        R.color.colorConservation3
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
                    ryl_conservationParent.setBackgroundResource(backgroundColorArray[i])
                    txtConservationTip.setText(tipsArray[i])

                    i++
                    if (i > imageArray.size - 1) {
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