package com.example.tracklep.Activities

import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.custom.DayAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_compare.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import java.io.IOException


class CompareActivity : BaseActivity(), OnChartValueSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        try {
            txtCABtitle.text = getString(R.string.my_account)
            imgCABback.setOnClickListener {
                finish()
            }

            clickPerform()

            var count = ArrayList<ResponseModelClasses.BarChart>()
            count.add(ResponseModelClasses.BarChart(10, 10.1f))
            count.add(ResponseModelClasses.BarChart(15, 7.1f))
            count.add(ResponseModelClasses.BarChart(5, 4.1f))
            count.add(ResponseModelClasses.BarChart(13, 9.1f))
            count.add(ResponseModelClasses.BarChart(17, 15.1f))
            count.add(ResponseModelClasses.BarChart(2, 3.1f))
            /* for (i in 0 until 10) {
                 count.add(ResponseModelClasses.BarChart(i + 1, i + .1f))
             }*/

//            barChartInit()
//            setData(count)
//            setData1()
            setChartData()
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

    private fun barChartInit() {
//        chart.setOnChartValueSelectedListener(this)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setFitBars(true)
//        chart.groupBars(0.1f, 0.1f, 0.1f)
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);
        val xAxisFormatter = DayAxisValueFormatter(chart)
        val xAxis = chart.xAxis
        xAxis.setCenterAxisLabels(true);
        xAxis.position = XAxisPosition.BOTTOM
//        xAxis.setTypeface(tfLight)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter
    }

    fun chartInit() {
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(false)

        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setGridBackgroundColor(resources.getColor(android.R.color.transparent))
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(true)
//        chart.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        val x = chart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        x.isEnabled = true
//        x.setTypeface(tfLight);
//        x.setLabelCount(1,true);
//        x.gridColor = resources.getColor(R.color.gray_color_100)
//        x.setAxisMinimum(1);
        x.textColor = Color.BLACK
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(true)
        x.axisLineColor = Color.BLUE


        val y = chart.axisLeft
//        y.setTypeface(tfLight);
//        y.setLabelCount(4, false);
        y.textColor = Color.BLACK
//        y.gridColor = resources.getColor(R.color.gray_color_100)
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        y.setDrawGridLines(true)
        y.axisLineColor = Color.BLUE

        chart.axisRight.isEnabled = false

//        // add data
//        seekBarY.setOnSeekBarChangeListener(this);
//        seekBarX.setOnSeekBarChangeListener(this);
//
//        // lower max, as cubic runs significantly slower than linear
//        seekBarX.setMax(700);
//
//        seekBarX.setProgress(45);
//        seekBarY.setProgress(100);

        chart.legend.isEnabled = false

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        chart.invalidate()
    }

    private fun setChartData() {
        val barEntries = ArrayList<BarEntry>()
        val barEntries1 = ArrayList<BarEntry>()

        barEntries.add(BarEntry(1f, 989.21f))
        barEntries.add(BarEntry(2f, 420.22f))
        barEntries.add(BarEntry(3f, 758f))
        barEntries.add(BarEntry(4f, 3078.97f))
        barEntries.add(BarEntry(5f, 4200.96f))
        barEntries.add(BarEntry(6f, 400.4f))
        barEntries.add(BarEntry(7f, 5888.58f))

        barEntries1.add(BarEntry(1f, 950f))
        barEntries1.add(BarEntry(2f, 791f))
        barEntries1.add(BarEntry(3f, 630f))
        barEntries1.add(BarEntry(4f, 782f))
        barEntries1.add(BarEntry(5f, 2714.54f))
        barEntries1.add(BarEntry(6f, 500f))
        barEntries1.add(BarEntry(7f, 2173.36f))


        val barDataSet = BarDataSet(barEntries, "This Year")
        barDataSet.color = Color.parseColor("#F44336")
        val barDataSet1 = BarDataSet(barEntries1, "Previous Year")
        barDataSet1.setColors(Color.parseColor("#9C27B0"))
        barDataSet1.setColors(Color.parseColor("#e241f4"))
        barDataSet1.setColors(Color.parseColor("#42f46e"))

        val months = arrayOf("TYPE 1", "TYPE 2", "TYPE 3", "TYPE 4")
        val data = BarData(barDataSet, barDataSet1)
        chart.data = data

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        chart.axisLeft.axisMinimum = 0f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(true)
        xAxis.isGranularityEnabled = true
        xAxis.setDrawAxisLine(false)

//        xAxis.setTypeface(tfLight)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7


        val barSpace = 0.02f
        val groupSpace = 0.3f
        val groupCount = 4

        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(false)

        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setGridBackgroundColor(resources.getColor(android.R.color.transparent))
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(true)

        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setFitBars(true)
//        chart.groupBars(0.1f, 0.1f, 0.1f)
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        //IMPORTANT *****
        data.barWidth = 0.15f
        chart.getXAxis().axisMinimum = 0f
        chart.getXAxis().axisMaximum = 0 + chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
        chart.groupBars(0f, groupSpace, barSpace) // pe
    }

    @Throws(IndexOutOfBoundsException::class, NullPointerException::class)
    private fun setData(allData: ArrayList<ResponseModelClasses.BarChart>) {
        try {

            var start = 1f


            val values = ArrayList<BarEntry>()

            for (i in allData.indices) {
                values.add(
                    BarEntry(
                        java.lang.Float.parseFloat(allData.get(i).range.toString()),
                        java.lang.Float.parseFloat(allData.get(i).count.toString())
                    )
                )
                start += (allData.get(i).range)
            }

            val set1: BarDataSet

            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = values
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()

            } else {
                set1 = BarDataSet(values, "The year 2017")

                set1.setDrawIcons(false)

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

                /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

                val startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
                val startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light)
                val startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
                val startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light)
                val startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light)
                val endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark)
                val endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple)
                val endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
                val endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark)
                val endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark)

                val gradientColors = ArrayList<GradientColor>()
                gradientColors.add(GradientColor(startColor1, endColor1))
                gradientColors.add(GradientColor(startColor2, endColor2))
                gradientColors.add(GradientColor(startColor3, endColor3))
                gradientColors.add(GradientColor(startColor4, endColor4))
                gradientColors.add(GradientColor(startColor5, endColor5))

                set1.gradientColors = gradientColors

                val dataSets = ArrayList<IBarDataSet>()
                dataSets.add(set1)

                val data = BarData(dataSets)
                data.setValueTextSize(10f)
//                data.setValueTypeface(tfLight)
                data.barWidth = 0.9f

                chart.data = data
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            throw IOException("Only supported for index 0 to 10")
        } catch (e: IOException) {
            e.printStackTrace()
        }

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
            chart.getBarBounds(e as BarEntry, bounds)
            val position = chart.getPosition(e, AxisDependency.LEFT)

            Log.i("bounds", bounds.toString())
            Log.i("position", position.toString())

            Log.i(
                "x-index",
                "low: " + chart.lowestVisibleX + ", high: "
                        + chart.highestVisibleX
            )

            MPPointF.recycleInstance(position)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /*internal fun compareMeBarChartData(
        currentvalues: List<Float>, previousvalues: List<Float>,
        xVals: java.util.ArrayList<String>, ylabel: String, colorSet1: String, colorSet2: String
    ): BarData? {

        var d: BarData? = null
        try {
            var f: Float?

            // ----first line------------------------------------------------------
            val floatArray = FloatArray(currentvalues.size)
            for (i in currentvalues.indices) {
                f = currentvalues[i]
                floatArray[i] = f ?: java.lang.Float.NaN
            }

            val yVals1 = java.util.ArrayList<BarEntry>()
            for (i in floatArray.indices) {
                if (floatArray[i] > 0) {
                    val usagevalue = floatArray[i]

                    if (usagevalue > 0.0)
                        yVals1.add(BarEntry(usagevalue, i.toFloat()))
                } else
                    yVals1.add(BarEntry(-1f, i.toFloat()))
            }

            val set1 = BarDataSet(yVals1, "")
            set1.setDrawValues(true)
            set1.valueTextSize = 10f
            set1.color = Color.parseColor(colorSet1)
            set1.valueTextColor = Color.parseColor(colorSet1)

            /// ---second
            /// line------------------------------------------------------------
            val floatArray2 = FloatArray(previousvalues.size)
            for (i in previousvalues.indices) {
                f = previousvalues[i]
                floatArray2[i] = f ?: java.lang.Float.NaN
            }

            val yVals2 = java.util.ArrayList<BarEntry>()
            for (i in floatArray2.indices) {
                if (floatArray2[i] > 0) {
                    val usagevalue2 = floatArray2[i]

                    if (usagevalue2 > 0.0)
                        yVals2.add(BarEntry(usagevalue2, i.toFloat()))
                } else
                    yVals2.add(BarEntry(-1f, i.toFloat()))
            }

            val set2 = BarDataSet(yVals2, ylabel)
            set2.color = Color.parseColor(colorSet2)
            set2.setDrawValues(true)

            set1.axisDependency = YAxis.AxisDependency.LEFT
            set2.axisDependency = YAxis.AxisDependency.LEFT

            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            dataSets.add(set2)

            val barSpace = 0.02f
            val groupSpace = 0.3f
            val groupCount = 4

            var d = BarData(xVals, dataSets)
//            d.setGroupSpace(80f)
            d.groupBars(0f, 80f,bar) // pe
            d.setValueTextSize(10f)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return d
    }*/


}
