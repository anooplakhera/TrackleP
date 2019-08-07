package com.example.tracklep.Activities

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.custom_action_bar.*

class UsageActivity : BaseActivity(), OnChartValueSelectedListener {

    val bar1 = ArrayList<ResponseModelClasses.BarChart>()
    val bar2 = ArrayList<ResponseModelClasses.BarChart>()
    val xVals = ArrayList<String>()

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

            initDummyValue()

            clickPerform()

            setChartData(bar1, bar2, xVals)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initDummyValue() {
        bar1.add(ResponseModelClasses.BarChart(1f, 989.21f))
        bar1.add(ResponseModelClasses.BarChart(2f, 420.22f))
        bar1.add(ResponseModelClasses.BarChart(3f, 758f))
        bar1.add(ResponseModelClasses.BarChart(4f, 3078.97f))
        bar1.add(ResponseModelClasses.BarChart(5f, 4200.96f))
        bar1.add(ResponseModelClasses.BarChart(6f, 400.4f))
        bar1.add(ResponseModelClasses.BarChart(7f, 5888.58f))
        bar1.add(ResponseModelClasses.BarChart(8f, 5888.58f))

        bar2.add(ResponseModelClasses.BarChart(1f, 950f))
        bar2.add(ResponseModelClasses.BarChart(2f, 791f))
        bar2.add(ResponseModelClasses.BarChart(3f, 630f))
        bar2.add(ResponseModelClasses.BarChart(4f, 782f))
        bar2.add(ResponseModelClasses.BarChart(5f, 2714.54f))
        bar2.add(ResponseModelClasses.BarChart(6f, 500f))
        bar2.add(ResponseModelClasses.BarChart(7f, 2173.36f))
        bar2.add(ResponseModelClasses.BarChart(8f, 2173.36f))

        xVals.add("Jan")
        xVals.add("Feb")
        xVals.add("Mar")
        xVals.add("Apr")
        xVals.add("May")
        xVals.add("Jun")
        xVals.add("July")
        xVals.add("Aug")
    }

    private fun clickPerform() {
        txtCCF.setOnClickListener {
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtCCF.setTextColor(resources.getColor(R.color.colorWhite))
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtGallon.setTextColor(resources.getColor(R.color.colorBlack))
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtDollar.setTextColor(resources.getColor(R.color.colorBlack))

            bar1.shuffle()
            bar2.shuffle()
            setChartData(bar1, bar2, xVals)
        }
        txtGallon.setOnClickListener {
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtGallon.setTextColor(resources.getColor(R.color.colorWhite))
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtCCF.setTextColor(resources.getColor(R.color.colorBlack))
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtDollar.setTextColor(resources.getColor(R.color.colorBlack))

            bar1.shuffle()
            bar2.shuffle()
            setChartData(bar1, bar2, xVals)
        }
        txtDollar.setOnClickListener {
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtDollar.setTextColor(resources.getColor(R.color.colorWhite))
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtCCF.setTextColor(resources.getColor(R.color.colorBlack))
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtGallon.setTextColor(resources.getColor(R.color.colorBlack))

            bar1.shuffle()
            bar2.shuffle()
            setChartData(bar1, bar2, xVals)
        }
    }


    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>
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

        for (i in 0 until bar1.size) {
            yVals1.add(BarEntry(bar1.get(i).count, bar1.get(i).range))
        }

        for (i in 0 until bar2.size) {
            yVals2.add(BarEntry(bar2.get(i).count, bar2.get(i).range))
        }


        val set1 = BarDataSet(yVals1, "This year")
        set1.color = resources.getColor(R.color.colorCompareThisYear)
        val set2 = BarDataSet(yVals2, "Previous year")
        set2.color = resources.getColor(R.color.colorComparePreviousYear)
        val data = BarData(set1, set2)
        data.setValueFormatter(LargeValueFormatter())
        chartUsage.data = data
        chartUsage.barData.barWidth = barWidth
        chartUsage.xAxis.axisMinimum = 0F
        chartUsage.xAxis.axisMaximum = 0 + chartUsage.barData.getGroupWidth(groupSpace, barSpace) * year.size

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
        l.textSize = 8f

        //X-axis
        val xAxis = chartUsage.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.axisMaximum = 6f
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


}