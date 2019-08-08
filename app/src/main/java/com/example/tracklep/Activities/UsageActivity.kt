package com.example.tracklep.Activities

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.UserMeterListData
import com.example.tracklep.DataClasses.WaterUsageData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.MyBarDataSet
import com.example.tracklep.Utils.MyValueFormatter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.custom_action_bar.*

class UsageActivity : BaseActivity(), OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)

        try {
            txtCABtitle.text = getString(R.string.track_usage)
            imgCABadd.visibility = View.VISIBLE

            imgCABback.setOnClickListener {
                finish()
            }
            imgCABadd.setOnClickListener {
                startActivity(Intent(this, UsageNotificationActivity::class.java))
            }



            clickPerform()

            setChartData(
                WaterUsageData.getUsageConsumedBar(),
                WaterUsageData.getUsageBar(),
                WaterUsageData.getUsageEmptyBar(),
                WaterUsageData.getMonthList(),
                getString(R.string.c_this_year),
                getString(R.string.c_previous_year),
                getString(R.string.c_previous_year)
            )

            setupSpinner()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun clickPerform() {
        txtCCF.setOnClickListener {
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtCCF.setTextColor(resources.getColor(R.color.colorWhite))
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtGallon.setTextColor(resources.getColor(R.color.colorBlack))
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtDollar.setTextColor(resources.getColor(R.color.colorBlack))


        }
        txtGallon.setOnClickListener {
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtGallon.setTextColor(resources.getColor(R.color.colorWhite))
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtCCF.setTextColor(resources.getColor(R.color.colorBlack))
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtDollar.setTextColor(resources.getColor(R.color.colorBlack))


        }
        txtDollar.setOnClickListener {
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtDollar.setTextColor(resources.getColor(R.color.colorWhite))
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtCCF.setTextColor(resources.getColor(R.color.colorBlack))
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtGallon.setTextColor(resources.getColor(R.color.colorBlack))


        }


    }

    private fun setupSpinner() {
        spinnerMeter!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, UserMeterListData.getMeterNumberList())
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeter!!.setAdapter(aa)
    }


    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        bar3: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>,
        label1: String, label2: String, label3: String
    ) {

        val barWidth: Float = 0.3f
        val barSpace: Float = 0f
        val groupSpace: Float = 0.4f

        chartUsage.description = null;
        chartUsage.setPinchZoom(false);
        chartUsage.setScaleEnabled(false);
        chartUsage.setDrawBarShadow(false);
        chartUsage.setDrawGridBackground(false);
        chartUsage.animateXY(500, 500);

        val yVals1 = ArrayList<BarEntry>()
        val yVals2 = ArrayList<BarEntry>()
        val yVals3 = ArrayList<BarEntry>()

        for (i in 0 until bar1.size) {
            yVals1.add(BarEntry(bar1[i].range, bar1[i].count))
        }

        for (i in 0 until bar2.size) {
            yVals2.add(BarEntry(bar2[i].range, bar2[i].count))
        }
        for (i in 0 until bar3.size) {
            yVals3.add(BarEntry(bar3[i].range, bar3[i].count))
        }

        val set1 = MyBarDataSet(yVals1, label1)
//        val set1 = BarDataSet(yVals1, label1)
        var list = arrayListOf<Int>(R.color.colorLightGray, R.color.colorAppGray)
        set1.colors = list;

//        for (i in 0 until bar1.size) {
//            if (bar1[i].range < bar1[i].count) {
//                set1.getColor(resources.getColor(R.color.colorUsageWithin))
//            } else {
//                set1.getColor(resources.getColor(R.color.colorUsageOver))
////                set1.color = resources.getColor(R.color.colorUsageOver)
//            }
//        }
        val set2 = BarDataSet(yVals2, label2)
        set2.color = resources.getColor(R.color.colorUsageAllocation)
        /* val set3 = BarDataSet(yVals2, label3)
         set3.color = resources.getColor(R.color.colorUsageOver)*/
        val data = BarData(set1, set2)


//        data.setValueFormatter(LargeValueFormatter() as ValueFormatter?)
        data.setValueFormatter(MyValueFormatter() as ValueFormatter?)

        chartUsage.data = data
        chartUsage.barData.barWidth = barWidth
        chartUsage.xAxis.axisMinimum = 0F
        chartUsage.xAxis.axisMaximum = 0 + chartUsage.barData.getGroupWidth(groupSpace, barSpace) * year.size

        chartUsage.setVisibleXRangeMaximum(20F); // allow 20 values to be displayed at once on the x-axis, not more
        chartUsage.moveViewToX(10F);

        chartUsage.groupBars(0F, groupSpace, barSpace)
        chartUsage.data.isHighlightEnabled = false
        chartUsage.invalidate()

        val l = chartUsage.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.yOffset = 20f
        l.xOffset = 0f
        l.yEntrySpace = 0f
        l.textSize = 10f

        //X-axis
        val xAxis = chartUsage.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
//        xAxis.axisMaximum = 6f
        xAxis.labelCount = year.size
        xAxis.axisMaximum = year.size.toFloat()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(year)

        //Y-axis
        chartUsage.axisRight.isEnabled = false
        val leftAxis = chartUsage.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f
    }

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val onValueSelectedRectF = RectF()

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        try {

            if (e == null)
                return

            val bounds = onValueSelectedRectF
            chartUsage.getBarBounds(e as BarEntry, bounds)
            val position = chartUsage.getPosition(e, YAxis.AxisDependency.LEFT)

            Log.i("bounds", bounds.toString())
            Log.i("position", position.toString())

            Log.i(
                "x-index",
                "low: " + chartUsage.lowestVisibleX + ", high: "
                        + chartUsage.highestVisibleX
            )

            MPPointF.recycleInstance(position)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }


}