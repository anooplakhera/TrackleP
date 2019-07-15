package com.example.tracklep.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tracklep.BaseActivities.BaseFragment
import com.example.tracklep.R

class UtilityBillFragment : BaseFragment() {
    override fun setUp(view: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_utility_bill, container, false)
    }


}
