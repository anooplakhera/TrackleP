package com.example.tracklep.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import com.example.tracklep.custom.MyMarkerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsageActivity : BaseActivity(), OnChartValueSelectedListener {
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mulitBarChart: BarChart? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.i("Activity", "Nothing selected.")
    }

    fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if (h != null) {
            Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)

        try {
            txtCABtitle.text = getString(R.string.my_account)
            imgCABadd.visibility = View.VISIBLE
            imgCABback.setOnClickListener {
                finish()
            }



            mulitBarChart!!.setOnChartValueSelectedListener(this)
            mulitBarChart!!.getDescription().setEnabled(false)

//        chart.setDrawBorders(true);

            // scaling can now only be done on x- and y-axis separately
            mulitBarChart!!.setPinchZoom(false)

            mulitBarChart!!.setDrawBarShadow(false)

            mulitBarChart!!.setDrawGridBackground(false)

            // create a custom MarkerView (extend MarkerView) and specify the layout
            // to use for it
            val mv = MyMarkerView(this, R.layout.custom_marker_view)
            mv.setChartView(mulitBarChart) // For bounds control
            mulitBarChart!!.setMarker(mv) // Set the marker to the chart
//
//            seekBarX.setProgress(10)
//            seekBarY.setProgress(100)

            val l = mulitBarChart!!.getLegend()
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
            l.setOrientation(Legend.LegendOrientation.VERTICAL)
            l.setDrawInside(true)
            //l.typeface = tfLight
            l.yOffset = 0f
            l.xOffset = 10f
            l.yEntrySpace = 0f
            l.textSize = 8f

            val xAxis = mulitBarChart!!.getXAxis()
            //xAxis.typeface = tfLight
            xAxis.setGranularity(1f)
            xAxis.setCenterAxisLabels(true)
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }

            val leftAxis = mulitBarChart!!.getAxisLeft()
           // leftAxis.typeface = tfLight
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

            mulitBarChart!!.getAxisRight().isEnabled = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}