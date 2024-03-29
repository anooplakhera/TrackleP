package com.aquatrax.tracklep.Activities

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aquatrax.hp.togelresultapp.Preferences.AppPrefences
import com.aquatrax.tracklep.ApiClient.ApiClient
import com.aquatrax.tracklep.ApiClient.ApiInterface
import com.aquatrax.tracklep.ApiClient.ApiUrls
import com.aquatrax.tracklep.BaseActivities.BaseActivity
import com.aquatrax.tracklep.DataClasses.CompareSpendingData
import com.aquatrax.tracklep.DataModels.ResponseModelClasses
import com.aquatrax.tracklep.R
import com.aquatrax.tracklep.Utils.AppLog
import com.aquatrax.tracklep.Utils.MyValueFormatter
import com.aquatrax.tracklep.Utils.RequestClass
import com.aquatrax.tracklep.Utils.Utils
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
    var selectedComparision = "Me"
    var selectedAlpha = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        try {
            txtCABtitle.text = getString(R.string.compare)
            imgCABback.setOnClickListener {
                finish()
            }

            getCompareDetails()

            clickPerform()
            getLegendView("MC")

            resetAlpha()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun clickPerform() {
        try {
            txtCCF.setOnClickListener {
                txtGallon.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))
                txtCCF.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_selected))
                txtDollar.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))

                unitName = "K"
                getCompareDetails()
                txtChartDesc.setText(R.string.compare_ccf)
                resetAlpha()
                getLegendView("MC")
            }
            txtGallon.setOnClickListener {
                txtGallon.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_selected))
                txtCCF.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))
                txtDollar.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))

                unitName = "G"
                getCompareDetails()
                txtChartDesc.setText(R.string.compare_gallon)
                resetAlpha()
                getLegendView("MC")

            }

            txtDollar.setOnClickListener {
                txtGallon.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))
                txtCCF.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_unselected))
                txtDollar.setBackground(resources.getDrawable(R.drawable.tab_rounded_corner_selected))

                unitName = "D"
                getCompareDetails()
                txtChartDesc.setText(R.string.compare_dollar)
                resetAlpha()
                getLegendView("MC")

            }

            lytCompareMe.setOnClickListener {
                selectedComparision = "Me"
                setChartData(
                    CompareSpendingData.getCurrentBar(),
                    CompareSpendingData.getPreviousBar(),
                    CompareSpendingData.getYearBar(),
                    getString(R.string.c_this_year),
                    getString(R.string.c_previous_year)
                )
                resetAlpha()
                getLegendView("MC")

                txt_date_from_to.text = CompareSpendingData.getCompareMeTitle()
            }

            lytCompareZip.setOnClickListener {
                selectedComparision = "Zip"
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

                getLegendView("CN")
                txt_date_from_to.text = CompareSpendingData.getZipTitle()
            }

            lytCompareUtility.setOnClickListener {
                selectedComparision = "Util"
                setChartData(
                    CompareSpendingData.getCurrentBar(),
                    CompareSpendingData.getUtilityBar(),
                    CompareSpendingData.getYearBar(),
                    getString(R.string.c_this_year),
                    getString(R.string.c_utility)
                )
                lytCompareZip.alpha = 1.0f
                lytCompareMe.alpha = 1.0f
                lytCompareUtility.alpha = selectedAlpha
                lytCompareAll.alpha = 1.0f

                getLegendView("CE")
                txt_date_from_to.text = CompareSpendingData.getUtilityTitle()
            }

            lytCompareAll.setOnClickListener {
                selectedComparision = "All"
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

                getLegendView("CV")
                lytCompareAll.alpha = selectedAlpha
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.CompareDataResponse> =
                apiService.getCompareSpendingDetails(
                    getHeader(),
                    ApiUrls.getJSONRequestBody(
                        RequestClass.getMeterDetailsRequestModel(
                            AppPrefences.getAccountNumber(this),
                            AppPrefences.getDataBaseInfo(this)!!
                        )
                    ),
                    AppPrefences.getAccountNumber(this), unitName
                )
            call.enqueue(object : Callback<ResponseModelClasses.CompareDataResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.CompareDataResponse>,
                    response: Response<ResponseModelClasses.CompareDataResponse>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("getCompareDetails: " + Gson().toJson(response.body()));
                            CompareSpendingData.addArrayList1(response.body()!!.Results.Table)
                            CompareSpendingData.addArrayList2(response.body()!!.Results.Table1)
                            CompareSpendingData.addArrayList3(response.body()!!.Results.Table2)
                            CompareSpendingData.addArrayList4(response.body()!!.Results.Table3)
                            CompareSpendingData.addArrayList5(response.body()!!.Results.Table4)

                            txt_date_from_to.text = CompareSpendingData.getCompareMeTitle()
                            setChartData(
                                CompareSpendingData.getCurrentBar(),
                                CompareSpendingData.getPreviousBar(),
                                CompareSpendingData.getYearBar(),
                                getString(R.string.c_this_year),
                                getString(R.string.c_previous_year)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.CompareDataResponse>,
                    t: Throwable
                ) {
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

    private fun getLegendView(type: String) {
        if (type == "MC") {
            lytLegendGreen.visibility = View.VISIBLE
            lytLegendRed.visibility = View.VISIBLE
            lytLegendBlueDark.visibility = View.GONE
            lytLegendBlueLight.visibility = View.GONE
        } else if (type == "CN") {
            lytLegendGreen.visibility = View.VISIBLE
            lytLegendRed.visibility = View.GONE
            lytLegendBlueDark.visibility = View.VISIBLE
            lytLegendBlueLight.visibility = View.GONE
        } else if (type == "CE") {
            lytLegendGreen.visibility = View.VISIBLE
            lytLegendRed.visibility = View.GONE
            lytLegendBlueDark.visibility = View.GONE
            lytLegendBlueLight.visibility = View.VISIBLE
        } else if (type == "CV") {
            lytLegendGreen.visibility = View.VISIBLE
            lytLegendRed.visibility = View.VISIBLE
            lytLegendBlueDark.visibility = View.VISIBLE
            lytLegendBlueLight.visibility = View.VISIBLE
        }
    }

    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>,
        label1: String, label2: String
    ) {

        try {
            val barWidth = 0.2f
            val barSpace = 0f
            val groupSpace = 0.6f

            chart.clear()
            chart.description = null;
            chart.setPinchZoom(false);
            chart.setScaleEnabled(true);
            chart.setDrawBarShadow(false);
            chart.isHorizontalScrollBarEnabled = true
            chart.setVisibleXRangeMaximum(3f)
            chart.setDrawGridBackground(false);
            chart.canScrollHorizontally(1)
            chart.animateXY(1000, 1000);

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
            if (selectedComparision == "Me")
                set2.color = resources.getColor(R.color.colorComparePreviousYear)
            else if (selectedComparision == "Zip")
                set2.color = resources.getColor(R.color.colorCompareZip)
            else if (selectedComparision == "Util")
                set2.color = resources.getColor(R.color.colorCompareUtility)

            val data = BarData(set1, set2)
//        data.setValueFormatter(LargeValueFormatter() as ValueFormatter?)
            data.setValueFormatter(MyValueFormatter() as ValueFormatter?)

            chart.data = data
            chart.barData.barWidth = barWidth
            chart.xAxis.axisMinimum = 0F
            chart.xAxis.axisMaximum =
                0 + chart.barData.getGroupWidth(groupSpace, barSpace) * year.size

            chart.setVisibleXRangeMaximum(4F); // allow 20 values to be displayed at once on the x-axis, not more
            chart.moveViewToX(0F);

            chart.groupBars(0F, groupSpace, barSpace)
            chart.data.isHighlightEnabled = false
            chart.setFitBars(true)

            chart.legend.isEnabled = false;   // Hide the legend
            chart.isDoubleTapToZoomEnabled = false //Disable double click zoom


            val l = chart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.yOffset = 20f
            l.xOffset = 0f
            l.yEntrySpace = 0f
            l.textSize = 7f

            //X-axis
            val xAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f
//        xAxis.axisMaximum = 6f
//        xAxis.textSize = 9f
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

            chart.invalidate()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        bar3: ArrayList<ResponseModelClasses.BarChart>,
        bar4: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>
    ) {

        try {
            val barWidth = 0.2f
            val barSpace = 0.0f
            val groupSpace = 0.2f

            chart.clear()
            chart.description = null;
            chart.setPinchZoom(false);
            chart.setScaleEnabled(true);
            chart.setDrawBarShadow(false);
            chart.setDrawGridBackground(false);
            chart.setVisibleXRangeMaximum(3f)
            chart.isHorizontalScrollBarEnabled = true
            chart.canScrollHorizontally(1)
            chart.animateXY(1000, 1000)

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
            chart.xAxis.axisMaximum =
                0 + chart.barData.getGroupWidth(groupSpace, barSpace) * year.size

            //chart.setVisibleXRangeMaximum(3F); // allow 20 values to be displayed at once on the x-axis, not more
            chart.moveViewToX(0F);

            chart.groupBars(0F, groupSpace, barSpace)
            chart.data.isHighlightEnabled = false
            chart.setFitBars(true)

            chart.legend.isEnabled = false;   // Hide the legend
            chart.isDoubleTapToZoomEnabled = false //Disable double click zoom

            val l = chart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.yOffset = 20f
            l.xOffset = 0f
            l.yEntrySpace = 0f
            l.textSize = 7f

            //X-axis
            val xAxis = chart.xAxis

            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f
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

            chart.invalidate()
        } catch (e: Exception) {
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

}
