package com.example.tracklep.Activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.Adapter.SwipeItemAdapter
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
import kotlinx.android.synthetic.main.dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UsageActivity : BaseActivity(), OnChartValueSelectedListener,
    AdapterView.OnItemSelectedListener {

    private var selectedAlpha = 0.5f
    private var mType = "W"
    private var mMode = "B"
    private var selectedUnit = "CCF"
    private var selectedMeter = ""
    private var filterDate = ""
    var myCalendar = Calendar.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        try {
            txtCABtitle.text = getString(R.string.track_usage)

            imgCABback.setOnClickListener {
                finish()
            }

            imgCABadd.setBackgroundResource(R.drawable.usage_notification)
            imgCABadd.setOnClickListener {
                startActivity(Intent(this, UsageNotificationActivity::class.java))
            }


            clickPerform()

            checkIsAMI()

            txtUsageChartDesc.setText(R.string.usage_ccf)
            txtusage_disclaimer.text = getString(R.string.usage_disclaimer)

            // create an OnDateSetListener
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
                ) {
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                }
            }
            imgCalendar!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    var picker = DatePickerDialog(
                        this@UsageActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                    )
                    picker.datePicker.maxDate = System.currentTimeMillis()

                    picker.show()


                }

            })


            //setupSpinner()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        showToast(sdf.format(myCalendar.getTime()))
        filterDate = sdf.format(myCalendar.getTime())
        AppLog.printLog(filterDate)
        if (mMode == "H") {
            getWaterUsageHourly()
        } else {
            getWaterUsage()
        }
//        textview_date!!.text = sdf.format(cal.getTime())
    }

    private fun checkIsAMI() {
        try {
            if (AppPrefences.getIsAMI(this) == null) {
                getMeterDetailsAMI()
            } else {
                spinnerMeter.text = AppPrefences.getMeterNumber(this)
                if (AppPrefences.getIsAMI(this) == false) {
                    lytBiMonthly.alpha = selectedAlpha
                    lytHourly.visibility = View.GONE
                    lytDaily.visibility = View.GONE
                    lytMonthly.visibility = View.GONE
                    mMode = "B"
                    imgCABadd.visibility = View.GONE
                    getWaterUsage()
                } else {
                    lytBiMonthly.alpha = selectedAlpha
                    lytHourly.visibility = View.VISIBLE
                    lytDaily.visibility = View.VISIBLE
                    lytMonthly.visibility = View.VISIBLE
                    getWaterUsage()
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

            /* imgCalendar.setOnClickListener {
                 var picker = DatePickerDialog(
                     this@UsageActivity,
                     date(),
                     // set DatePickerDialog to point to today's date when it loads up
                     myCalendar.get(Calendar.YEAR),
                     myCalendar.get(Calendar.MONTH),
                     myCalendar.get(Calendar.DAY_OF_MONTH)
                 )
                 picker.datePicker.maxDate = System.currentTimeMillis()
                 picker.show()


             }*/
            // create an OnDateSetListener

            txtCCF.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_selected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_unselected)

                mType = "W"
                mMode = "B"
                resetAlpha()
//                checkIsAMI()
                selectedUnit = "CCF"
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_ccf)
                txtusage_disclaimer.text = getString(R.string.usage_disclaimer)
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            txtGallon.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_selected)
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_unselected)

                mType = "G"
                mMode = "B"
                resetAlpha()
//                checkIsAMI()
                selectedUnit = "Gallons"
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_gallon)
                txtusage_disclaimer.text = getString(R.string.usage_disclaimer)
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            txtDollar.setOnClickListener {
                txtGallon.background = getDrawable(R.drawable.tab_rounded_corner_unselected);
                txtCCF.background = getDrawable(R.drawable.tab_rounded_corner_unselected)
                txtDollar.background = getDrawable(R.drawable.tab_rounded_corner_selected)

                mType = "D"
                mMode = "B"
                resetAlpha()
//                checkIsAMI()
                selectedUnit = "$"
                getWaterUsage()
                txtUsageChartDesc.setText(R.string.usage_dollar)
                txtusage_disclaimer.text = getString(R.string.u_dollar_disclaimer)
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }

            }

            lytHourly.setOnClickListener {
                lytHourly.alpha = selectedAlpha
                lytDaily.alpha = 1.0f
                lytMonthly.alpha = 1.0f
                lytBiMonthly.alpha = 1.0f
//                resetAlpha()
                mMode = "H"
//                checkIsAMI()
                imgCalendar.visibility = View.VISIBLE
                getWaterUsageHourly()
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            lytDaily.setOnClickListener {
                lytDaily.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytMonthly.alpha = 1.0f
                lytBiMonthly.alpha = 1.0f
                imgCalendar.visibility = View.VISIBLE
                mMode = "D"
//                checkIsAMI()
                getWaterUsage()
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            lytMonthly.setOnClickListener {
                lytMonthly.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytDaily.alpha = 1.0f
                lytBiMonthly.alpha = 1.0f

                imgCalendar.visibility = View.INVISIBLE
                mMode = "M"
//                checkIsAMI()
                getWaterUsage()
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            lytBiMonthly.setOnClickListener {
                lytBiMonthly.alpha = selectedAlpha
                lytHourly.alpha = 1.0f
                lytMonthly.alpha = 1.0f
                lytDaily.alpha = 1.0f

                imgCalendar.visibility = View.INVISIBLE
                mMode = "B"
//                checkIsAMI()
                getWaterUsage()
                if (mType == "D" || mMode == "H") {
                    lytLegends.visibility = View.GONE
                } else {
                    lytLegends.visibility = View.VISIBLE
                }
            }

            imgCalendar.setOnClickListener {
                getSelectedDate()
            }

            r_lyt_meterList.setOnClickListener {
                openDialogList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

                var abc: ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne> =
                    WaterUsageData.mArrayListHourly!!

                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text =
                        selectedUnit + "" + abc.get(abc.size - 1)?.HIGHEST

                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + abc.get(abc.size - 1)?.LOWEST
                    txtSoFartThisMonthValue.text = selectedUnit + "" +
                            abc.get(abc.size - 1)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            abc.get(abc.size - 1)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        abc.get(abc.size - 1)?.HIGHEST + " " + selectedUnit

                    txtLowestThisPeriodValue.text =
                        abc.get(abc.size - 1)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        abc.get(abc.size - 1)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        abc.get(abc.size - 1)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
                //txt_date_from_to_usage.text = abc?.get(0)?.UsageDate
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod(mMode)
                }
            } else {
                var def: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne> =
                    WaterUsageData.mArrayList!!
                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.HIGHEST
                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.LOWEST
                    txtSoFartThisMonthValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            def?.get(def.size - 1)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        def?.get(def.size - 1)?.HIGHEST + " " + selectedUnit
                    txtLowestThisPeriodValue.text =
                        def?.get(def.size - 1)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        def?.get(def.size - 1)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        def?.get(def.size - 1)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod(mMode)
                }
            }
            chartUsage.clear()
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
            chartUsage.setFitBars(true)
//            chartUsage.setScaleEnabled(false);
//            chartUsage.isDoubleTapToZoomEnabled = false;

            chartUsage.legend.isEnabled = false;   // Hide the legend
            chartUsage.isDoubleTapToZoomEnabled = false //Disable double click zoom


            val l = chartUsage.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
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

            chartUsage.invalidate()
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

            if (mMode == "H") {
                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text = selectedUnit + "" +
                            WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST
                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + WaterUsageData.mArrayListHourly?.get(0)?.LOWEST
                    txtSoFartThisMonthValue.text = selectedUnit + "" +
                            WaterUsageData.mArrayListHourly?.get(0)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST + " " + selectedUnit
                    txtLowestThisPeriodValue.text =
                        WaterUsageData.mArrayListHourly?.get(0)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        WaterUsageData.mArrayListHourly?.get(0)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        WaterUsageData.mArrayListHourly?.get(0)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
            } else {
                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text =
                        selectedUnit + "" + WaterUsageData.mArrayList?.get(0)?.HIGHEST
                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + WaterUsageData.mArrayList?.get(0)?.LOWEST
                    txtSoFartThisMonthValue.text =
                        selectedUnit + "" + WaterUsageData.mArrayList?.get(0)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            WaterUsageData.mArrayList?.get(0)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        WaterUsageData.mArrayList?.get(0)?.HIGHEST + " " + selectedUnit
                    txtLowestThisPeriodValue.text =
                        WaterUsageData.mArrayList?.get(0)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        WaterUsageData.mArrayList?.get(0)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        WaterUsageData.mArrayList?.get(0)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod(mMode)
            }

            chartUsage.clear()
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

            // chartUsage.groupBars(0F, groupSpace, barSpace)
            chartUsage.data.isHighlightEnabled = false


            chartUsage.legend.isEnabled = false;   // Hide the legend
            chartUsage.isDoubleTapToZoomEnabled = false //Disable double click zoom
            val l = chartUsage.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
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
            chartUsage.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSingleGraphChartData(
        bar1: ArrayList<ResponseModelClasses.BarChart>,
        year: ArrayList<String>
    ) {


        val barWidth = 0.3f
        val barSpace = 0f
        val groupSpace = 0.4f
        try {

            if (mMode == "H") {

                var abc: ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne> =
                    WaterUsageData.mArrayListHourly!!

                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text =
                        selectedUnit + "" + abc.get(abc.size - 1)?.HIGHEST

                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + abc.get(abc.size - 1)?.LOWEST
                    txtSoFartThisMonthValue.text = selectedUnit + "" +
                            abc.get(abc.size - 1)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            abc.get(abc.size - 1)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        abc.get(abc.size - 1)?.HIGHEST + " " + selectedUnit

                    txtLowestThisPeriodValue.text =
                        abc.get(abc.size - 1)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        abc.get(abc.size - 1)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        abc.get(abc.size - 1)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
                //txt_date_from_to_usage.text = abc?.get(0)?.UsageDate
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod(mMode)
                }
            } else {
                var def: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne> = WaterUsageData.mArrayList!!
                if (selectedUnit == "$") {
                    txtHighestThisPeriodValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.HIGHEST
                    txtLowestThisPeriodValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.LOWEST
                    txtSoFartThisMonthValue.text =
                        selectedUnit + "" + def?.get(def.size - 1)?.TotalValue
                    txtProjectedUsageValue.text = selectedUnit + "" +
                            def?.get(def.size - 1)?.HIGHEST//TO BE UPDATED
                } else {
                    txtHighestThisPeriodValue.text =
                        def?.get(def.size - 1)?.HIGHEST + " " + selectedUnit
                    txtLowestThisPeriodValue.text =
                        def?.get(def.size - 1)?.LOWEST + " " + selectedUnit
                    txtSoFartThisMonthValue.text =
                        def?.get(def.size - 1)?.TotalValue + " " + selectedUnit
                    txtProjectedUsageValue.text =
                        def?.get(def.size - 1)?.HIGHEST + " " + selectedUnit//TO BE UPDATED
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txt_date_from_to_usage.text = WaterUsageData.getUsagePeriod(mMode)
                }
            }


            chartUsage.clear()
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

            for (i in 0 until bar1.size) {
//                yVals1.add(BarEntry(bar1[i].range, bar1[i].count))
                yVals1.add(BarEntry(0f, bar1[i].count/* bar1[i].count*/))
            }
            AppLog.printLog("ArrayOfGraph", Gson().toJson(yVals1))
            AppLog.printLog("ArrayOfGraphSize", yVals1.size.toString())
            AppLog.printLog("ArrayOfGraphSizeYear", year.size.toString())


            /*val set2 = BarDataSet(yVals1, "")
            val data = BarData(set2)
            chartUsage.data = data
            set2.color = resources.getColor(R.color.colorUsageWithin)
            set2.valueTextColor = Color.BLACK;
            set2.valueTextSize = 18f;*/

            val set2 = BarDataSet(yVals1, "")
            val data = BarData(set2)
            chartUsage.data = data
            set2.color = resources.getColor(R.color.colorUsageWithin)

            data.barWidth = 0.8f;
            data.setValueFormatter(MyValueFormatter() as ValueFormatter?)
            chartUsage.barData.barWidth = barWidth
            chartUsage.xAxis.axisMinimum = 0F
            chartUsage.xAxis.axisMaximum =
                0 + chartUsage.barData.getGroupWidth(groupSpace, barSpace) * year.size

//            chartUsage.setVisibleXRangeMaximum(5f); // allow 20 values to be displayed at once on the x-axis, not more
//            chartUsage.moveViewToX(5F);

//            chartUsage.groupBars(0F, groupSpace, barSpace)
            chartUsage.data.isHighlightEnabled = false
//            chartUsage.setFitBars(true)
//            chartUsage.setScaleEnabled(false);
//            chartUsage.isDoubleTapToZoomEnabled = false;

            chartUsage.legend.isEnabled = false;   // Hide the legend
            chartUsage.isDoubleTapToZoomEnabled = false //Disable double click zoom


            /*val l = chartUsage.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.yOffset = 20f
            l.xOffset = 0f
            l.yEntrySpace = 0f
            l.textSize = 10f*/

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

            chartUsage.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(8f, 0f))
        entries.add(BarEntry(2f, 1f))
        entries.add(BarEntry(5f, 2f))
        entries.add(BarEntry(20f, 3f))
        entries.add(BarEntry(15f, 4f))
        entries.add(BarEntry(19f, 5f))

        val barDataSet = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("18-Jan")
        labels.add("19-Jan")
        labels.add("20-Jan")
        labels.add("21-Jan")
        labels.add("22-Jan")
        labels.add("23-Jan")
        val data = BarData(barDataSet)
        chartUsage.data = data // set the data and list of lables into chart

//        chartUsage.setDescription("Set Bar Chart Description")  // set the description

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorAccent)

        chartUsage.animateY(5000)
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
                            spinnerMeter.text = UserMeterListData.getArrayItem(0).MeterNumber
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
            val apiService = ApiClient.getClient(ApiUrls.getBasePathUrl()).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.WaterUsages> = apiService.getWaterUsages(
                getHeader(),
                ApiUrls.getJSONRequestBody(
                    RequestClass.getWaterUsageRequestModel(
                        AppPrefences.getAccountNumber(this),
                        mType,
                        mMode,
                        filterDate,
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
                            var data = ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>()
                            data.addAll(response.body()!!.Results.Table)
                            //data.reverse()
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
                                    /*setSingleGraphChartData(
                                        WaterUsageData.getUsageConsumedBar(),
                                        *//*   WaterUsageData.getUsageBar(),
                                           WaterUsageData.getUsageEmptyBar(),*//*
                                        WaterUsageData.getMonthListMonthly()
//                                        getString(R.string.c_this_year),
//                                        getString(R.string.u_allocation)
                                    )*/
                                } else {
                                     setChartDataDollar(
                                         WaterUsageData.getUsageConsumedBar(),
                                         WaterUsageData.getUsageBar(),
                                         WaterUsageData.getUsageEmptyBar(),
                                         WaterUsageData.getMonthList(),
                                         getString(R.string.c_this_year),
                                         getString(R.string.u_allocation)
                                     )
                                    /*setSingleGraphChartData(
                                        WaterUsageData.getUsageConsumedBar(),
                                        *//*   WaterUsageData.getUsageBar(),
                                           WaterUsageData.getUsageEmptyBar(),*//*
                                        WaterUsageData.getMonthList()
//                                        getString(R.string.c_this_year),
//                                        getString(R.string.u_allocation)
                                    )*/
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
                            filterDate,
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
                                   /* setSingleGraphChartData(
                                        WaterUsageData.getUsageConsumedBarHourly(),
//                                        WaterUsageData.getUsageBarHourly(),
//                                        WaterUsageData.getUsageEmptyBarHourly(),
                                        WaterUsageData.getMonthListHourly()
                                    )*/
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

                                    /*setSingleGraphChartData(
                                        WaterUsageData.getUsageConsumedBarHourly(),
//                                        WaterUsageData.getUsageBarHourly(),
//                                        WaterUsageData.getUsageEmptyBarHourly(),
                                        WaterUsageData.getMonthListHourly()
                                    )*/
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
        lytHourly.alpha = 1.0f
        lytDaily.alpha = 1.0f
        lytMonthly.alpha = 1.0f
        lytBiMonthly.alpha = selectedAlpha
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

    private fun getSelectedDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this@UsageActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                //lblDate.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)
                Log.e("Selected Date", "" + dayOfMonth + " " + monthOfYear + ", " + year)
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
        dpd.show()
    }

    private fun openDialogList() {
        try {
            val dialog = Dialog(this@UsageActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.setTitle("Meter Number")
            dialog.show()
            dialog.txtTitleTop.text = title
            dialog.txtTitleTop.textSize = 13f

            val mSwipeItemAdapter =
                SwipeItemAdapter(UserMeterListData.getMeterNumberList()) { position ->

                    spinnerMeter.text = UserMeterListData.getArrayItem(position).MeterNumber
                    selectedMeter = UserMeterListData.getArrayItem(position).MeterNumber
                    spinnerMeter.text = selectedMeter
                    dialog.dismiss()
                    if (mMode == "H") {
                        getWaterUsageHourly()
                    } else {
                        getWaterUsage()
                    }

                }

            dialog.dialogRecycleView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            dialog.dialogRecycleView.layoutManager = LinearLayoutManager(this)
            dialog.dialogRecycleView.adapter = mSwipeItemAdapter

            /*val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(dialog.dialogRecycleView)*/
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}