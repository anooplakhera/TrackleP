package com.aquatrax.tracklep.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        hideSystemUI()

        Handler().postDelayed(Runnable {

            if (AppPrefences.getLogin(this)!! && AppPrefences.getRememberMe(this)!!) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
//                AppPrefences.clearAll(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2500)
    }


}
