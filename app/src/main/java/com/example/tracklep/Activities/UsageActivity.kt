package com.example.tracklep.Activities

import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiClient
import com.example.tracklep.ApiClient.ApiInterface
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.BaseActivities.BaseActivity
import com.example.tracklep.DataClasses.UserMeterListData
import com.example.tracklep.DataClasses.WaterUsageData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.R
import com.example.tracklep.Utils.*
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_usage.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsageActivity : BaseActivity(), OnChartValueSelectedListener,
    AdapterView.OnItemSelectedListener {

    private var selectedAlpha = 0.5f
    private var mType = "W"
    private var mMode = "H"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        try {
            txtCABtitle.text = getString(R.string.track_usage)


            imgCABback.setOnClickListener {
                finish()
            }
            imgCABadd.setOnClickListener {
                startActivity(Intent(this, UsageNotificationActivity::class.java))
            }


            clickPerform()

            checkIsAMI()

            txtUsageChartDesc.setText(R.string.usage_ccf)
            txtusage_disclaimer.text = getString(R.string.usage_disclaimer)

            setupSpinner()


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkIsAMI() {
        try {
            if (AppPrefences.getIsAMI(this) == null) {
                getMeterDetailsAMI()
            } else {
                if (AppPrefences.getIsAMI(this) == false) {
                    lytBiMonthly.alpha = selectedAlpha
                    lytHourly.visibility = View.GONE
                    lytDaily.visibility = View.GONE
                    lytMonthly.visibility = View.GONE
                    mMode = "B"
                    imgCABadd.visibility = View.GONE
                    getWaterUsage()
                } else {
                    lytHourly.visibility = View.VISIBLE
                    lytDaily.visibility = View.VISIBLE
                    lytMonthly.visibility = View.VISIBLE
                    getWaterUsageHourly()
                    imgCABadd.visibility = View.VISIBLE
                    //resetAlpha()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            txtCCF.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_selected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_unselected)

                mType = "W"
                checkIsAMI()
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_ccf)
                txtusage_disclaimer.text = getString(R.string.usage_disclaimer)

            }

            txtGallon.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_selected)
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_unselected)

                mType = "G"
                checkIsAMI()
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_gallon)
                txtusage_disclaimer.text = getString(R.string.usage_disclaimer)
            }

            txtDollar.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_unselected);
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_selected)

                mType = "D"
                checkIsAMI()
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_dollar)
                txtusage_disclaimer.text = getString(R.string.u_dollar_disclaimer)

            }

            lytHourly.setOnClickListener {
                resetAlpha()
                mMode = "H"
                checkIsAMI()
                getWaterUsageHourly()
            }

            lytDaily.setOnClickListener {
                lytDaily.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytMonthly.alpha = 1.0f
                lytBiMonthly.alpha = 1.0f

                mMode = "D"
                checkIsAMI()
                getWaterUsage()
            }

            lytMonthly.setOnClickListener {
                lytMonthly.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytDaily.alpha = 1.0f
                lytBiMonthly.alpha = 1.0f

                mMode = "M"
                checkIsAMI()
                getWaterUsage()
            }

            lytBiMonthly.setOnClickListener {
                lytBiMonthly.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytMonthly.alpha = 1.0f
                lytDaily.alpha = 1.0f

                mMode = "B"
                checkIsAMI()
                getWaterUsage()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setupSpinner() {
        spinnerMeter!!.onItemSelectedListener = this
        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            UserMeterListData.getMeterNumberList()
        )
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeter!!.adapter = aa
    }

    private fun setChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        bar3: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>,
        label1: String, label2: String
    ) {
        try {

            val barWidth = 0.3f
            val barSpace = 0f
            val groupSpace = 0.4f

            if (mMode == "H") {
                txtHighestThisPeriodValue.text = WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST
                txtLowestThisPeriodValue.text = WaterUsageData.mArrayListHourly?.get(0)?.LOWEST
                txtSoFartThisMonthValue.text = WaterUsageData.mArrayListHourly?.get(0)?.TotalValue
                txtProjectedUsageValue.text =
                    WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST//TO BE UPDATED

                txt_date_from_to_usage.text = WaterUsageData.mArrayListHourly?.get(0)?.UsageDate
            } else {
                txtHighestThisPeriodValue.text = WaterUsageData.mArrayList?.get(0)?.HIGHEST
                txtLowestThisPeriodValue.text = WaterUsageData.mArrayList?.get(0)?.LOWEST
                txtSoFartThisMonthValue.text = WaterUsageData.mArrayList?.get(0)?.TotalValue
                txtProjectedUsageValue.text =
                    WaterUsageData.mArrayList?.get(0)?.HIGHEST//TO BE UPDATED

                txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod()
            }


            chartUsage.description = null
            chartUsage.setPinchZoom(false)
            chartUsage.setScaleEnabled(false)
            chartUsage.setDrawBarShadow(false)
            chartUsage.setDrawGridBackground(false)
            chartUsage.setVisibleXRangeMaximum(5f)
            chartUsage.isHorizontalScrollBarEnabled = true
            chartUsage.canScrollHorizontally(1)
            chartUsage.animateXY(1000, 1000)

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

            val set1 = MyBarDataSet(yVals1, bar2, label1)
            var list = arrayListOf(
                ContextCompat.getColor(this, R.color.colorUsageWithin),
                ContextCompat.getColor(this, R.color.colorUsageOver)
            )
            set1.colors = list
            var listLabel = arrayOf("Usage Within", "Usage Over")
            set1.stackLabels = listLabel

//Use for label


            val set2 = BarDataSet(yVals2, label2)
            set2.color = resources.getColor(R.color.colorUsageAllocation)
            val data = BarData(set1, set2)

            data.setValueFormatter(MyValueFormatter() as ValueFormatter?)

            chartUsage.data = data
            chartUsage.barData.barWidth = barWidth
            chartUsage.xAxis.axisMinimum = 0F
            chartUsage.xAxis.axisMaximum =
                0 + chartUsage.barData.getGroupWidth(groupSpace, barSpace) * year.size

            chartUsage.setVisibleXRangeMaximum(5f); // allow 20 values to be displayed at once on the x-axis, not more
            chartUsage.moveViewToX(5F);

            chartUsage.groupBars(0F, groupSpace, barSpace)
            chartUsage.data.isHighlightEnabled = false
            chartUsage.invalidate()


            val l = chartUsage.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
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
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f
            //Y-axis
            chartUsage.axisRight.isEnabled = false
            val leftAxis = chartUsage.axisLeft
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setChartDataDollar(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        bar2: ArrayList<ResponseModelClasses.BarChart>,
        bar3: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>,
        label1: String, label2: String
    ) {
        try {

            val barWidth = 0.3f
            val barSpace = 0f
            val groupSpace = 0.4f

            txtHighestThisPeriodValue.text = WaterUsageData.mArrayList?.get(0)?.HIGHEST
            txtLowestThisPeriodValue.text = WaterUsageData.mArrayList?.get(0)?.LOWEST
            txtSoFartThisMonthValue.text = WaterUsageData.mArrayList?.get(0)?.TotalValue
            txtProjectedUsageValue.text = WaterUsageData.mArrayList?.get(0)?.HIGHEST//TO BE UPDATED

            txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod()

            chartUsage.description = null;
            chartUsage.setPinchZoom(false);
            chartUsage.setScaleEnabled(false);
            chartUsage.setDrawBarShadow(false);
            chartUsage.setDrawGridBackground(false);
            chartUsage.setVisibleXRangeMaximum(5f)
            chartUsage.isHorizontalScrollBarEnabled = true
            chartUsage.canScrollHorizontally(1)
            chartUsage.animateXY(1000, 1000);

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


            val set1 = BarDataSet(yVals1, label1)
            set1.getColor(resources.getColor(R.color.colorUsageWithin))


//            val set1 = MyBarDataSet(yVals1, bar2, "")
//            var list = arrayListOf(
//                ContextCompat.getColor(this, R.color.colorUsageWithin),
//                ContextCompat.getColor(this, R.color.colorUsageOver)
//            )
//            set1.colors = list
//            var listLabel = arrayOf("Usage Within", "Usage Over")
//            set1.stackLabels = listLabel

//        }


//

//Use for label


            val set2 = BarDataSet(yVals2, label2)
            set2.color = resources.getColor(R.color.colorUsageAllocation)
            val data = BarData(set1, set2)

            data.setValueFormatter(MyValueFormatter() as ValueFormatter?)

            chartUsage.data = data
            chartUsage.barData.barWidth = barWidth
            chartUsage.xAxis.axisMinimum = 0F
            chartUsage.xAxis.axisMaximum =
                0 + chartUsage.barData.getGroupWidth(groupSpace, barSpace) * year.size

            chartUsage.setVisibleXRangeMaximum(5f); // allow 20 values to be displayed at once on the x-axis, not more
            chartUsage.moveViewToX(10F);

            chartUsage.groupBars(0F, groupSpace, barSpace)
            chartUsage.data.isHighlightEnabled = false
            chartUsage.invalidate()

            val l = chartUsage.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
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
            xAxis.textSize = 7f
            xAxis.labelRotationAngle = -45f
            //Y-axis
            chartUsage.axisRight.isEnabled = false
            val leftAxis = chartUsage.axisLeft
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMeterDetailsAMI() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.MeterDetails> = apiService.getMeterDetails(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getMeterDetailsRequestModel(
                        AppPrefences.getAccountNumber(this), AppPrefences.getDataBaseInfo(this)!!
                    )
                ),
                AppPrefences.getAccountNumber(this)
            )
            call.enqueue(object : Callback<ResponseModelClasses.MeterDetails> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.MeterDetails>,
                    response: Response<ResponseModelClasses.MeterDetails>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("MeterDetailsResponse: " + Gson().toJson(response.body()));
                            UserMeterListData.clearArrayList()
                            UserMeterListData.addArrayList(response.body()!!.Results.Table)
                            getWaterUsage()

                            AppPrefences.setIsAMI(
                                this@UsageActivity,
                                response.body()!!.Results.Table.get(0).IsAMI
                            )
                            checkIsAMI()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.MeterDetails>,
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
        //dismissDialog()
        showToast(getString(R.string.internet))
    }

    private fun getWaterUsage() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.WaterUsages> = apiService.getWaterUsages(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getWaterUsageRequestModel(
                        AppPrefences.getAccountNumber(this),
                        mType,
                        mMode,
                        AppPrefences.getDataBaseInfo(this)!!
                    )
                )
            )
            call.enqueue(object : Callback<ResponseModelClasses.WaterUsages> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.WaterUsages>,
                    response: Response<ResponseModelClasses.WaterUsages>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("WaterDetails: " + Gson().toJson(response.body()));
                            var data =
                                ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>()
                            data.addAll(response.body()!!.Results.Table)
                            data.reverse()
                            WaterUsageData.clearArrayList()
                            WaterUsageData.addArrayList(data)


                            if (mType == "D") {

                                if (mMode == "M") {
                                    setChartDataDollar(
                                        WaterUsageData.getUsageConsumedBar(),
                                        WaterUsageData.getUsageBar(),
                                        WaterUsageData.getUsageEmptyBar(),
                                        WaterUsageData.getMonthListMonthly(),
                                        getString(R.string.c_this_year),
                                        getString(R.string.u_allocation)
                                    )
                                } else {
                                    setChartDataDollar(
                                        WaterUsageData.getUsageConsumedBar(),
                                        WaterUsageData.getUsageBar(),
                                        WaterUsageData.getUsageEmptyBar(),
                                        WaterUsageData.getMonthList(),
                                        getString(R.string.c_this_year),
                                        getString(R.string.u_allocation)
                                    )
                                }
                            } else {
                                if (mMode == "M") {
                                    setChartData(
                                        WaterUsageData.getUsageConsumedBar(),
                                        WaterUsageData.getUsageBar(),
                                        WaterUsageData.getUsageEmptyBar(),
                                        WaterUsageData.getMonthListMonthly(),
                                        getString(R.string.u_usage),
                                        getString(R.string.u_allocation)
                                    )
                                } else {
                                    setChartData(
                                        WaterUsageData.getUsageConsumedBar(),
                                        WaterUsageData.getUsageBar(),
                                        WaterUsageData.getUsageEmptyBar(),
                                        WaterUsageData.getMonthList(),
                                        getString(R.string.u_usage),
                                        getString(R.string.u_allocation)
                                    )
                                }
                            }


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseModelClasses.WaterUsages>, t: Throwable) {
                    AppLog.printLog("Failure()- ", t.message.toString())
                    dismissDialog()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }
    } else {
        //dismissDialog()
        showToast(getString(R.string.internet))
    }


    private fun getWaterUsageHourly() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.WaterUsagesHourly> =
                apiService.getWaterUsagesHourly(
                    getHeader(),
                    ApiUrls.getJSONRequestBody(
                        RequestClass.getWaterUsageRequestModel(
                            AppPrefences.getAccountNumber(this),
                            mType,
                            mMode,
                            AppPrefences.getDataBaseInfo(this)!!
                        )
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.WaterUsagesHourly> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.WaterUsagesHourly>,
                    response: Response<ResponseModelClasses.WaterUsagesHourly>
                ) {
                    try {
                        dismissDialog()
                        if (response.body() != null) {
                            AppLog.printLog("WaterDetails: " + Gson().toJson(response.body()));
                            var data =
                                ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne>()
                            data.addAll(response.body()!!.Results.Table)
                            data.reverse()
                            WaterUsageData.clearArrayList()
                            WaterUsageData.addArrayListHourly(data)


                            if (mType == "D") {
                                if (mMode == "H") {
                                    setChartDataDollar(
                                        WaterUsageData.getUsageConsumedBarHourly(),
                                        WaterUsageData.getUsageBarHourly(),
                                        WaterUsageData.getUsageEmptyBarHourly(),
                                        WaterUsageData.getMonthListHourly(),
                                        getString(R.string.c_this_year),
                                        getString(R.string.u_allocation)
                                    )
                                }
                            } else {
                                if (mMode == "H") {
                                    setChartData(
                                        WaterUsageData.getUsageConsumedBarHourly(),
                                        WaterUsageData.getUsageBarHourly(),
                                        WaterUsageData.getUsageEmptyBarHourly(),
                                        WaterUsageData.getMonthListHourly(),
                                        getString(R.string.u_usage),
                                        getString(R.string.u_allocation)
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.WaterUsagesHourly>,
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
        //dismissDialog()
        showToast(getString(R.string.internet))
    }

    private fun resetAlpha() {

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

