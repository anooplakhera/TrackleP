package com.example.tracklep.Activities

import android.os.Bundle
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.R
import kotlinx.android.synthetic.main.custom_action_bar.*

class MyProfile : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        txtCABtitle.text = getString(R.string.my_account)
        imgCABback.setOnClickListener {
            finish()
        }
    }
}
