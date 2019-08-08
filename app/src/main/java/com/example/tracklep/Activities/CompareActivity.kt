package com.example.tracklep.Activities

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.CompareSpendingData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.MyValueFormatter
import com.example.tracklep.Utils.RequestClass
import com.example.tracklep.Utils.Utils
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_compare.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CompareActivity : BaseActivity(), OnChartValueSelectedListener {

    var unitName = "K"
    var selectedAlpha = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        try {
            txtCABtitle.text = getString(R.string.compare_spending)
            imgCABback.setOnClickListener {
                finish()
            }

            getCompareDetails()

            clickPerform()

            resetAlpha()

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

            unitName = "K"
            getCompareDetails()
            txtChartDesc.setText(R.string.compare_ccf)
            resetAlpha()
        }
        txtGallon.setOnClickListener {
            txtGallon.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txtGallon.setTextColor(resources.getColor(R.color.colorWhite))
            txtCCF.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtCCF.setTextColor(resources.getColor(R.color.colorBlack))
            txtDollar.setBackgroundColor(resources.getColor(R.color.colorWhite))
            txtDollar.setTextColor(resources.getColor(R.color.colorBlack))

            unitName = "G"
            getCompareDetails()
            txtChartDesc.setText(R.string.compare_gallon)
            resetAlpha()

        }

        lytCompareMe.setOnClickListener {
            setChartData(
                CompareSpendingData.getCurrentBar(),
                CompareSpendingData.getPreviousBar(),
                CompareSpendingData.getYearBar(),
                getString(R.string.c_this_year),
                getString(R.string.c_previous_year)
            )
            resetAlpha()
            txt_date_from_to.text = CompareSpendingData.getCompareMeTitle()
        }

        lytCompareZip.setOnClickListener {
            setChartData(
                CompareSpendingData.getCurrentBar(),
                CompareSpendingData.getZipBar(),
                CompareSpendingData.getYearBar(),
                getString(R.string.c_this_year),
                getString(R.string.c_zip)
            )
            lytCompareZip.alpha = selectedAlpha
            lytCompareMe.alpha = 1.0f
            lytCompareUtility.alpha = 1.0f
            lytCompareAll.alpha = 1.0f
            txt_date_from_to.text = CompareSpendingData.getZipTitle()
        }

        lytCompareUtility.setOnClickListener {
            setChartData(
                CompareSpendingData.getCurrentBar(),
                CompareSpendingData.getUtilityBar(),
                CompareSpendingData.getYearBar(),
                getString(R.string.c_this_year),
                getString(R.string.c_utility)
            )
            lytCompareZip.alpha = 1.0f
            lytCompareMe.alpha = 1.0f
            lytCompareUtility.alpha =selectedAlpha
            lytCompareAll.alpha = 1.0f
            txt_date_from_to.text = CompareSpendingData.getUtilityTitle()
        }

        lytCompareAll.setOnClickListener {
            setChartData(
                CompareSpendingData.getCurrentBar(),
                CompareSpendingData.getPreviousBar(),
                CompareSpendingData.getUtilityBar(),
                CompareSpendingData.getZipBar(),
                CompareSpendingData.getYearBar()
            )
            lytCompareZip.alpha = 1.0f
            lytCompareMe.alpha = 1.0f
            lytCompareUtility.alpha = 1.0f
            lytCompareAll.alpha = selectedAlpha
        }
    }

    private fun resetAlpha() {
        lytCompareMe.alpha = selectedAlpha
        lytCompareZip.alpha = 1.0f
        lytCompareUtility.alpha = 1.0f
        lytCompareAll.alpha = 1.0f
    }

    private fun getCompareDetails() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.CompareDataResponse> = apiService.getCompareSpendingDetails(
                getHeader(), unitName,
                ApiUrls.getJSONRequestBody(RequestClass.getMeterDetailsRequestModel(AppPrefences.getLoginUserInfo(this).AccountNumber)),
                AppPrefences.getLoginUserInfo(this).AccountNumber
            )
            call.enqueue(object : Callback<ResponseModelClasses.CompareDataResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.CompareDataResponse>,
                    response: Response<ResponseModelClasses.CompareDataResponse>
                ) {
                    dismissDialog()
                    if (response.body() != null) {
                        CompareSpendingData.addArrayList1(response.body()!!.Results.Table)
                        CompareSpendingData.addArrayList2(response.body()!!.Results.Table1)
                        CompareSpendingData.addArrayList3(response.body()!!.Results.Table2)
                        CompareSpendingData.addArrayList4(response.body()!!.Results.Table3)
                        CompareSpendingData.addArrayList5(response.body()!!.Results.Table4)
                        AppLog.printLog("getCompareDetails: " + Gson().toJson(response.body()));

                        setChartData(
                            CompareSpendingData.getCurrentBar(),
                            CompareSpendingData.getPreviousBar(),
                            CompareSpendingData.getYearBar(),
                            getString(R.string.c_this_year),
                            getString(R.string.c_previous_year)
                        )
                        txt_date_from_to.text = CompareSpendingData.getCompareMeTitle()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.CompareDataResponse>, t: Throwable) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        showToast(getString(R.string.internet))
    }

    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>,
        label1: String, label2: String
    ) {

        val barWidth: Float = 0.3f
        val barSpace: Float = 0f
        val groupSpace: Float = 0.4f

        chart.description = null;
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.animateXY(500, 500);

        val yVals1 = ArrayList<BarEntry>()
        val yVals2 = ArrayList<BarEntry>()

        for (i in 0 until bar1.size) {
            yVals1.add(BarEntry(bar1[i].range, bar1[i].count))
        }

        for (i in 0 until bar2.size) {
            yVals2.add(BarEntry(bar2[i].range, bar2[i].count))
        }

        val set1 = BarDataSet(yVals1, label1)
        set1.color = resources.getColor(R.color.colorCompareThisYear)
        val set2 = BarDataSet(yVals2, label2)
        set2.color = resources.getColor(R.color.colorComparePreviousYear)
        val data = BarData(set1, set2)
//        data.setValueFormatter(LargeValueFormatter() as ValueFormatter?)
        data.setValueFormatter(MyValueFormatter() as ValueFormatter?)

        chart.data = data
        chart.barData.barWidth = barWidth
        chart.xAxis.axisMinimum = 0F
        chart.xAxis.axisMaximum = 0 + chart.barData.getGroupWidth(groupSpace, barSpace) * year.size

        chart.setVisibleXRangeMaximum(20F); // allow 20 values to be displayed at once on the x-axis, not more
        chart.moveViewToX(10F);

        chart.groupBars(0F, groupSpace, barSpace)
        chart.data.isHighlightEnabled = false
        chart.invalidate()

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.yOffset = 20f
        l.xOffset = 0f
        l.yEntrySpace = 0f
        l.textSize = 10f

        //X-axis
        val xAxis = chart.xAxis
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
        chart.axisRight.isEnabled = false
        val leftAxis = chart.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f
    }

    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        bar3: ArrayList<ResponseModelClasses.BarChart>,
        bar4: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>
    ) {

        val barWidth: Float = 0.3f
        val barSpace: Float = 0f
        val groupSpace: Float = 0.4f
        val groupCount: Int = 4

        chart.description = null;
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.animateXY(500, 500);

        val yVals1 = ArrayList<BarEntry>()
        val yVals2 = ArrayList<BarEntry>()
        val yVals3 = ArrayList<BarEntry>()
        val yVals4 = ArrayList<BarEntry>()

        for (i in 0 until bar1.size) {
            yVals1.add(BarEntry(bar1.get(i).range, bar1.get(i).count))
        }
        for (i in 0 until bar2.size) {
            yVals2.add(BarEntry(bar2.get(i).range, bar2.get(i).count))
        }
        for (i in 0 until bar2.size) {
            yVals3.add(BarEntry(bar3.get(i).range, bar3.get(i).count))
        }
        for (i in 0 until bar2.size) {
            yVals4.add(BarEntry(bar4.get(i).range, bar3.get(i).count))
        }


        val set1 = BarDataSet(yVals1, "This year")
        set1.color = resources.getColor(R.color.colorCompareThisYear)
        val set2 = BarDataSet(yVals2, "Previous year")
        set2.color = resources.getColor(R.color.colorComparePreviousYear)
        val set3 = BarDataSet(yVals3, "Utility")
        set3.color = resources.getColor(R.color.colorCompareUtility)
        val set4 = BarDataSet(yVals4, "Zip")
        set4.color = resources.getColor(R.color.colorCompareZip)
        val data = BarData(set1, set2, set3, set4)
//        data.setValueFormatter(LargeValueFormatter())
        data.setValueFormatter(MyValueFormatter() as ValueFormatter?)
        chart.data = data
        chart.barData.barWidth = barWidth
        chart.xAxis.axisMinimum = 0F
        chart.xAxis.axisMaximum = 0 + chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount

        chart.setVisibleXRangeMaximum(20F); // allow 20 values to be displayed at once on the x-axis, not more
        chart.moveViewToX(10F);

        chart.groupBars(0F, groupSpace, barSpace)
        chart.data.isHighlightEnabled = false
        chart.invalidate()

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.yOffset = 20f
        l.xOffset = 0f
        l.yEntrySpace = 0f
        l.textSize = 10f

        //X-axis
        val xAxis = chart.xAxis
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
        chart.axisRight.isEnabled = false
        val leftAxis = chart.axisLeft
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

}
