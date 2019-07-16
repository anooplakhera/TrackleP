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

class ConservationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conservation)

        try {
            txtCABtitle.text = getString(R.string.water_conversation)
            imgCABback.setOnClickListener {
                finish()
            }


            /*AnimationDrawable animation = new AnimationDrawable();
            animation.addFrame(getResources().getDrawable(R.drawable.image1), 100);
            animation.addFrame(getResources().getDrawable(R.drawable.image2), 500);
            animation.addFrame(getResources().getDrawable(R.drawable.image3), 300);
            animation.setOneShot(false);

            ImageView imageAnim =  (ImageView) findViewById(R.id.img);
            imageAnim.setBackgroundDrawable(animation);

            // start the animation!
            animation.start()*/

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}