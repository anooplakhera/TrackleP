/*
package com.example.tracklep.Activities

import android.app.AlertDialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView

import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.sus.scm.database.DBHelper
import com.sus.scm_ugi.Handler.ColorHandler
import com.sus.scm_ugi.Handler.CompareSpendingHandler
import com.sus.scm_ugi.Handler.NewCompare_Handler
import com.sus.scm_ugi.Handler.New_Compare_Handler
import com.sus.scm_ugi.Handler.UsagesDashboardDetailHandler
import com.sus.scm_ugi.R
import com.sus.scm_ugi.activity.CompareSpending_Screen
import com.sus.scm_ugi.dataset.ChartColorDataset
import com.sus.scm_ugi.dataset.CompareSpendingDataSet
import com.sus.scm_ugi.dataset.NewCompareSpending_Monthly_Dataset
import com.sus.scm_ugi.dataset.UsageMonthlydataset
import com.sus.scm_ugi.dataset.UsageMultiMeterDataset
import com.sus.scm_ugi.dataset.UsagesDashboardDetailDataset
import com.sus.scm_ugi.dataset.UsagesRangeDataset
import com.sus.scm_ugi.dataset.Usages_Gas_Monthly_dataset
import com.sus.scm_ugi.utilities.Constant
import com.sus.scm_ugi.utilities.DataEncryptDecrypt
import com.sus.scm_ugi.utilities.GlobalAccess
import com.sus.scm_ugi.utilities.MyValueFormatter
import com.sus.scm_ugi.utilities.RightAxisValueFormater
import com.sus.scm_ugi.utilities.SharedprefStorage
import com.sus.scm_ugi.utilities.VerticalTextView
import com.sus.scm_ugi.webservices.WebServicesPost

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Collections

*/
/**
 * Created by Tejasi.Vashishtha on 4/29/2016.
 *//*

class CompareSpending_Detail_Fragment : Fragment(), View.OnClickListener {

    private var tv_modulename: TextView? = null
    private var tv_editmode: TextView? = null
    private var tv_usagesofarvalue: TextView? = null
    private var tv_expectedusagevalue: TextView? = null

    private var li_fragmentlayout: LinearLayout? = null
    private var comparelegendlayout1: LinearLayout? = null
    private var ll_projected_usage: LinearLayout? = null

    internal var power_dollar: Boolean? = false
    internal var water_dollar: Boolean? = false
    internal var gas_dollar: Boolean? = false
    internal var water_gallon: Boolean? = false
    internal var compare_all: Boolean? = false
    internal var gas_CCF: Boolean? = false
    internal var compare_me: Boolean? = false
    internal var power_kwh: Boolean? = false
    internal var project_usage: Boolean? = false
    internal var summary: Boolean? = false
    internal var compare_utility: Boolean? = false
    internal var water_HCF: Boolean? = false
    internal var compare_zip: Boolean? = false
    internal var single_meter: Boolean? = true

    private var isMe_Zip_Utility_All = 0 // 0-Me, 1-Zip, 2-Utility, 3-All
    private var isKwh_Dollar_Gallon = 1 // 1- Kwh, 2- Dollar, 3-Gallon

    private var power_tab: Boolean? = true
    private var water_tab: Boolean? = false
    private var gas_tab: Boolean? = false
    private var power: Boolean? = false
    private var gas: Boolean? = false
    private var water: Boolean? = false
    private var LoginToken = ""
    private var tabselected: String? = "power"
    private var viewtype = ""
    var tv_back: TextView
    var tv_compareme: TextView
    var tv_comparezip: TextView
    var tv_compareutility: TextView
    var tv_compareall: TextView

    private var globalvariables: GlobalAccess? = null
    private var sharedpref: SharedprefStorage? = null
    private var DBNew: DBHelper? = null
    private var languageCode = ""
    private var ChartType = "Column"
    private var powerusagetentativestring = ""

    var monthlylowrangekwh = 0.0
    var monthlyhighrangekwh = 0.0
    var monthlylowrangedollar = 0.0
    var monthlyhighrangedollar = 0.0
    var monthlylowrangegallon = 0.0
    var monthlyhighrangegallon = 0.0

    internal var btn_dollar: Button
    internal var btn_kwh: Button
    internal var btn_gallon: Button
    private var progressdialog: ProgressDialog? = null

    private var cv_usagestats: LinearLayout? = null
    private var rel_monthselection: LinearLayout? = null
    private val monthslist = ArrayList<String>()
    var selectedmonth = 11
    internal var tv_disclaimer: TextView
    internal var tv_read_more: TextView
    internal var tv_selectedmonth: TextView
    internal var li_buttonslayout: LinearLayout
    internal var ll_top_buttons: LinearLayout
    internal var cv_legend: RelativeLayout
    internal var IsDollar = true

    //private RadioGroup radioGroupCompare;
    internal var dollar: Boolean? = false
    internal var kwh: Boolean? = false
    internal var gallon: Boolean? = false
    internal var tv_yaxistitle: VerticalTextView
    private var currentColor = "#000000"
    private var previousColor = "#000000"
    private var utilityColor = "#000000"
    private var zipColor = "#000000"
    private var commonSingleton: ColorHandler? = null
    private var months: Array<CharSequence>? = null
    private var isMonthSelected = false
    internal var meternumber = ""
    internal var mStrUnit = ""
    // Variables for Multimeter
    private var multiMeterName: Array<String>? = null
    private val arrListMultiMeter = ArrayList<UsageMultiMeterDataset>()

    internal var scroll: ScrollView
    internal var scrollCompanion: LinearLayout
    */
/*  use for chart data show in desimal or not *//*

    internal var isDecimal: Boolean? = true
    internal var session_code = ""
    internal var isAllModuleHide = false

    internal var CompareMeResultPower = ""
    internal var CompareMeResultGas = ""
    internal var CompareMeResult: String? = ""
    internal var compareSpendingLineChart: CombinedChart? = null

    private fun changeViewParent(removeViewGroup: ViewGroup, addViewGroup: ViewGroup) {

        if (isAllModuleHide) {
            removeViewGroup.visibility = View.GONE
        } else {
            val view = removeViewGroup.getChildAt(0)
            removeViewGroup.removeView(view)
            removeViewGroup.visibility = View.GONE

            addViewGroup.visibility = View.VISIBLE
            addViewGroup.addView(view)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {

        val rootView = inflater.inflate(
            R.layout.fragment_comparespending_detail, container, false
        ) as ViewGroup

        setCommonDate()
        initView(rootView)

        try {
            tv_modulename!!.setText(DBNew!!.getLabelText(getString(R.string.Compare), languageCode))
            tv_editmode!!.visibility = View.GONE

            isKwh_Dollar_Gallon = 2 // default is 2 for dollar
            isMe_Zip_Utility_All = 0

            try {
                isDecimal = java.lang.Boolean.parseBoolean(Constant.IsDecimal)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val bundle = arguments
            tabselected = bundle.getString("TabSelected")
            if (tabselected != null) {
                if (tabselected!!.equals("power", ignoreCase = true)) {
                    power_tab = true
                    water_tab = false
                    gas_tab = false
                } else if (tabselected!!.equals("water", ignoreCase = true)) {
                    water_tab = true
                    gas_tab = false
                    power_tab = false
                } else if (tabselected!!.equals("gas", ignoreCase = true)) {
                    gas_tab = true
                    water_tab = false
                    power_tab = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            btn_dollar.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewDollar), languageCode))
            if (tabselected!!.equals("power", ignoreCase = true))
                btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewkWh), languageCode))
            else
                btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewCCF), languageCode))
            btn_gallon.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewGL), languageCode))

            if (globalvariables!!.haveNetworkConnection(activity)) {
                val task = MultiMeterTask()
                task.applicationContext = activity
                task.execute()
            } else {
                if (progressdialog != null) progressdialog!!.cancel()
                globalvariables!!.Networkalertmessage(activity)
            }

            setHideShow() // initialize keys for hide show
            hideshowbuttons()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupFullviewGraphview(true)
        globalvariables!!.findAlltexts(rootView)
        return rootView
    }

    private fun initView(rootView: ViewGroup) {
        try {
            scroll = rootView.findViewById(R.id.scroll) as ScrollView
            scrollCompanion = rootView.findViewById(R.id.scrollCompanion) as LinearLayout

            tv_modulename = activity.findViewById(R.id.tv_modulename) as TextView
            tv_editmode = activity.findViewById(R.id.tv_editmode) as TextView
            tv_usagesofarvalue = rootView.findViewById(R.id.tv_usagesofarvalue) as TextView
            tv_expectedusagevalue = rootView.findViewById(R.id.tv_expectedusagevalue) as TextView
            tv_back = rootView.findViewById(R.id.tv_back) as TextView
            tv_selectedmonth = rootView.findViewById(R.id.tv_selectedmonth) as TextView
            tv_compareme = rootView.findViewById(R.id.tv_compareme) as TextView
            tv_comparezip = rootView.findViewById(R.id.tv_comparezip) as TextView
            tv_compareutility = rootView.findViewById(R.id.tv_compareutility) as TextView
            tv_compareall = rootView.findViewById(R.id.tv_compareall) as TextView
            tv_disclaimer = rootView.findViewById(R.id.tv_disclaimer) as TextView
            tv_read_more = rootView.findViewById(R.id.tv_read_more) as TextView
            compareSpendingLineChart = CombinedChart(activity)

            tv_yaxistitle = rootView.findViewById(R.id.tv_yaxistitle) as VerticalTextView
            dollar = true // It will be true first time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            li_buttonslayout = rootView.findViewById(R.id.li_buttonslayout) as LinearLayout
            ll_projected_usage = rootView.findViewById(R.id.ll_projected_usage) as LinearLayout

            comparelegendlayout1 = rootView.findViewById(R.id.comparelegendlayout1) as LinearLayout
            rel_monthselection = rootView.findViewById(R.id.rel_monthselection) as LinearLayout

            li_fragmentlayout = rootView.findViewById(R.id.li_fragmentlayout) as LinearLayout

            cv_usagestats = rootView.findViewById(R.id.cv_usagestats) as LinearLayout
            ll_top_buttons = rootView.findViewById(R.id.ll_top_buttons) as LinearLayout
            cv_legend = rootView.findViewById(R.id.cv_legend) as RelativeLayout

            btn_dollar = rootView.findViewById(R.id.btn_dollar) as Button
            btn_kwh = rootView.findViewById(R.id.btn_kwh) as Button
            btn_gallon = rootView.findViewById(R.id.btn_gallon) as Button

            tv_compareme.setOnClickListener(this)
            tv_comparezip.setOnClickListener(this)
            tv_compareutility.setOnClickListener(this)
            tv_compareall.setOnClickListener(this)
            rel_monthselection!!.setOnClickListener(this)
            btn_dollar.setOnClickListener(this)
            btn_gallon.setOnClickListener(this)
            btn_kwh.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setCommonDate() {
        try {
            globalvariables = activity.applicationContext as GlobalAccess
            sharedpref = SharedprefStorage.getInstance(activity)
            DBNew = DBHelper.getInstance(activity)
            languageCode = sharedpref!!.loadPreferences(Constant.LANGUAGE_CODE)
            viewtype = sharedpref!!.loadPreferences(Constant.LANDSCAPE_GRAPH_KEY)
            session_code = sharedpref!!.loadPreferences("sessioncode")
            LoginToken = sharedpref!!.loadPreferences(Constant.LoginToken)
            viewtype = sharedpref!!.loadPreferences(Constant.LANDSCAPE_GRAPH_KEY)

            // singleton class for call color service only one time
            commonSingleton = ColorHandler.getInstance()
            if (!commonSingleton!!.validateArrayList(commonSingleton!!.getArrayChartColorDataSet()))
                commonSingleton!!.getChartColorAsync(activity)
            setColorDataset()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View) {

        try {
            if (v === rel_monthselection) {
                MonthSelection_dialog()
            } else if (v === btn_dollar) {
                try {
                    dollar = true
                    kwh = false
                    gallon = false
                    mStrUnit = "$"
                    tv_yaxistitle.setText(
                        DBNew!!.getLabelText(
                            getString(R.string.Compare_CostOfUnitsConsumed),
                            languageCode
                        )
                    )
                    IsDollar = true
                    dollar_selected()
                    isKwh_Dollar_Gallon = 2
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }

            } else if (v === btn_kwh) {

                try {
                    dollar = false
                    kwh = true
                    gallon = false
                    if (tabselected!!.equals("gas", ignoreCase = true)) {
                        mStrUnit = "CCF"
                        tv_yaxistitle.setText("Units consumed (CCF)")
                    } else {
                        mStrUnit = "kWh"
                        tv_yaxistitle.setText(
                            DBNew!!.getLabelText(
                                getString(R.string.Compare_UnitsConsumed),
                                languageCode
                            )
                        )
                    }
                    isKwh_Dollar_Gallon = 1
                    kwh_selected()
                    IsDollar = false
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // *********************************************************************************************************************************
    private inner class MultiMeterTask : AsyncTask<String, Int, String>() {
        var applicationContext: Context? = null
        internal var response = "" // E for Power

        override fun onPreExecute() {
            super.onPreExecute()
            try {
                progressdialog = ProgressDialog.show(
                    activity,
                    null,
                    DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg strings: String): String {

            try {
                val accountnumber = sharedpref!!.loadPreferences(Constant.CONTRACT_NUMBER)
                val loginToken = sharedpref!!.loadPreferences(Constant.LoginToken)
                if (power_tab!!)
                    response = WebServicesPost.CompareGetPowerMultiMeterResult(accountnumber, loginToken, "E")
                if (gas_tab!!)
                    response = WebServicesPost.CompareGetGasMultiMeterResult(accountnumber, loginToken, "G")
            } catch (e: Exception) {
                progressdialog!!.cancel()
                e.printStackTrace()
            }

            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result != null) {
                try {
                    progressdialog!!.dismiss()
                    val objAll = UsageMultiMeterDataset()

                    arrListMultiMeter.add(0, objAll)

                    val dataDecrpyt = DataEncryptDecrypt()
                    val wholedata = JSONObject(result)

                    var wholeresult: String? = ""
                    if (power_tab!!)
                        wholeresult = wholedata.optString("GetPowerMeterNumberListMobResult")
                    if (gas_tab!!)
                        wholeresult = wholedata.optString("GetGasMeterNumberListMobResult")

                    if (wholeresult != null) {
                        try {
                            println("wholeresult : $wholeresult")
                            println("decrypted result : $wholeresult")
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }

                        val jarray = JSONArray(wholeresult)
                        for (i in 0 until jarray.length()) {
                            if (power_tab!!) {
                                if (i == 0)
                                    meternumber = jarray.getJSONObject(i).getString("PowerMeterNumber")
                                else
                                    meternumber =
                                        meternumber + "|" + jarray.getJSONObject(i).getString("PowerMeterNumber")
                            }
                            if (gas_tab!!) {
                                if (i == 0)
                                    meternumber = jarray.getJSONObject(i).getString("GasMeterNumber")
                                else
                                    meternumber =
                                        meternumber + "|" + jarray.getJSONObject(i).getString("GasMeterNumber")
                            }
                        }

                        if (jarray.length() > 1)
                            single_meter = false

                        //objAll.setMeterNumber("All");
                        objAll.setMeterNumber(meternumber)
                        objAll.setAmi(false)
                        objAll.setStatus("0")
                        objAll.setMeterTye("")
                        println("Meter Number: $meternumber")

                        */
/*loadPowerUsagetask newusage = new loadPowerUsagetask();
                        newusage.execute(meternumber);*//*

                        if (globalvariables!!.haveNetworkConnection(activity)) {
                            val task = CompareSpendingChartTypeTask()
                            task.applicationContext = activity
                            task.execute()

                        } else {
                            if (progressdialog != null) progressdialog!!.cancel()
                            globalvariables!!.Networkalertmessage(activity)
                        }
                    }
                } catch (e: Exception) {
                    progressdialog!!.cancel()
                    e.message
                }

                try {
                    // Store Meter number into String Array
                    multiMeterName = arrayOfNulls(arrListMultiMeter.size)
                    for (i in arrListMultiMeter.indices) {
                        multiMeterName[i] = arrListMultiMeter.get(i).getMeterNumber()
                    }
                } catch (e: Exception) {
                    progressdialog!!.cancel()
                    e.printStackTrace()
                }

            }
        }
    }

    //load which chart appears in compare spending: Line or Bar Chart
    //tejasi, 08/03/16
    private inner class CompareSpendingChartTypeTask : AsyncTask<Void, Void, String>() {
        var applicationContext: Context? = null
        internal var ChartTypeResult = ""

        override fun onPreExecute() {
            try {
                if (progressdialog != null) {
                    if (!progressdialog!!.isShowing)
                        progressdialog = ProgressDialog.show(
                            applicationContext,
                            null,
                            DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                        )
                } else {
                    progressdialog = ProgressDialog.show(
                        applicationContext,
                        null,
                        DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                    )

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg params: Void): String {

            try {
                ChartTypeResult = WebServicesPost.getCompareSpendingChartType()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ChartTypeResult
        }

        override fun onPostExecute(result: String) {

            super.onPostExecute(result)

            try {
                val jsonArray = JSONArray(result)
                val Chart = jsonArray.getJSONObject(0).optString("CompareChartType")

                if (!Chart.equals("", ignoreCase = true)) {
                    ChartType = Chart
                }
            } catch (e: JSONException) {
                ChartType = "Column"
                e.printStackTrace()
            }

        }
    }

    //============================  Asyntask to fetch data from server for POWER  ============================ for power data
    private inner class PowerWebServiceTask : AsyncTask<Void, Void, String>() {
        var applicationContext: Context? = null
        internal var result = ""

        override fun onPreExecute() {
            try {
                if (progressdialog != null) {
                    if (!progressdialog!!.isShowing)
                        progressdialog = ProgressDialog.show(
                            applicationContext,
                            null,
                            DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                        )
                } else {
                    progressdialog = ProgressDialog.show(
                        applicationContext,
                        null,
                        DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg params: Void): String {

            try {
                if (!usagerangedata.isEmpty()) {
                    usagerangedata.clear()
                }

                val accountnumber = sharedpref!!.loadPreferences(Constant.DEFAULTACCOUNTNUMBER)

                usagerangedata = WebServicesPost.loadUsagerange(accountnumber, LoginToken)
                powerusagetentativestring = WebServicesPost.loadusagesdashboarddetail(accountnumber, LoginToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)
            try {
                if (result != null) {
                    val dashboardhandler = UsagesDashboardDetailHandler()
                    dashboardhandler.setParserObjIntoObj(powerusagetentativestring)
                    expectedusagesdata = dashboardhandler.fetchpowerList()

                    setPowrUsageData()

                    for (i in usagerangedata.indices) {
                        if (usagerangedata.get(i).getRangeMode().equalsIgnoreCase("M") && usagerangedata.get(i).getType().equalsIgnoreCase(
                                "K"
                            )
                        ) {
                            monthlylowrangekwh = usagerangedata.get(i).getLowRange()
                            monthlyhighrangekwh = usagerangedata.get(i).getHighRange()
                        }
                        if (usagerangedata.get(i).getRangeMode().equalsIgnoreCase("M") && usagerangedata.get(i).getType().equalsIgnoreCase(
                                "D"
                            )
                        ) {
                            monthlylowrangedollar = usagerangedata.get(i).getLowRange()
                            monthlyhighrangedollar = usagerangedata.get(i).getHighRange()
                        }
                    }

                    try {
                        if (globalvariables!!.haveNetworkConnection(activity)) {

                            power = true
                            gas = false
                            water = false
                            val task = callDollarKWHWebServiceTask()
                            task.applicationContext = activity
                            task.execute()
                        } else {
                            if (progressdialog != null) progressdialog!!.cancel()
                            globalvariables!!.Networkalertmessage(activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    if (progressdialog != null) progressdialog!!.cancel()
                }
            } catch (e: Exception) {
                if (progressdialog != null) progressdialog!!.cancel()
                e.printStackTrace()
            }

        }
    }

    private fun setPowrUsageData() {
        try {
            var expectedbudget = ""
            try {
                if (expectedusagesdata != null && expectedusagesdata!!.size > 0) {
                    //  for (int i = 0; i < expectedusagesdata.size(); i++) {
                    val powerUsageSoFar = expectedusagesdata!![expectedusagesdata!!.size - 1].getPowerUsageSoFar()
                    val powerExpectedUsage = expectedusagesdata!![expectedusagesdata!!.size - 1].getPowerExpectedUsage()
                    if (dollar!!) {
                        tv_usagesofarvalue!!.text =
                            "$" + CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumptionAmount()
                        expectedbudget = mStrUnit + isConvertTODecimal(powerExpectedUsage)
                    } else if (kwh!!) {
                        val powerunitusagesofar =
                            isConvertTODecimal(expectedusagesdata!![expectedusagesdata!!.size - 1].getPowerunitusagesofar())
                        if (power!!)
                            tv_usagesofarvalue!!.setText(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption() + " kWh")
                        else if (gas!!)
                            tv_usagesofarvalue!!.setText(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption() + " CCF")
                        val powerunitexpectedusage =
                            expectedusagesdata!![expectedusagesdata!!.size - 1].getPowerunitexpectedusage()
                        expectedbudget = isConvertTODecimal(powerunitexpectedusage) + mStrUnit
                    }

                    tv_expectedusagevalue!!.text = expectedbudget
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //============================  Asyntask to fetch data from server for GAS ============================  for gas data
    private inner class GasWebServiceTask : AsyncTask<Void, Void, String>() {
        var applicationContext: Context? = null
        internal var result = ""

        override fun onPreExecute() {
            try {
                power = false
                gas = true
                water = false
                if (progressdialog != null) {
                    if (!progressdialog!!.isShowing)
                        progressdialog = ProgressDialog.show(
                            applicationContext,
                            null,
                            DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                        )
                } else {
                    progressdialog = ProgressDialog.show(
                        applicationContext,
                        null,
                        DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg params: Void): String {

            try {
                if (!usagerangedata.isEmpty()) {
                    usagerangedata.clear()
                }

                val accountnumber = sharedpref!!.loadPreferences(Constant.DEFAULTACCOUNTNUMBER)
                usagerangedata = WebServicesPost.loadUsagerange(accountnumber, LoginToken)
                powerusagetentativestring = WebServicesPost.loadusagesdashboarddetail(accountnumber, LoginToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)
            try {
                if (result != null) {
                    try {
                        val dashboardhandler = UsagesDashboardDetailHandler()
                        dashboardhandler.setParserObjIntoObj(powerusagetentativestring)
                        gasexpectedusagesdata = dashboardhandler.fetchGasList()

                        setGasUsageData()

                        //System.out.println("CS_dollar_currentdata.size :" + CS_dollar_currentdata.size());
                        for (i in usagerangedata.indices) {
                            if (usagerangedata.get(i).getRangeMode().equalsIgnoreCase("M") && usagerangedata.get(i).getType().equalsIgnoreCase(
                                    "K"
                                )
                            ) {
                                monthlylowrangekwh = usagerangedata.get(i).getLowRange()
                                monthlyhighrangekwh = usagerangedata.get(i).getHighRange()
                            }
                            if (usagerangedata.get(i).getRangeMode().equalsIgnoreCase("M") && usagerangedata.get(i).getType().equalsIgnoreCase(
                                    "D"
                                )
                            ) {
                                monthlylowrangedollar = usagerangedata.get(i).getLowRange()
                                monthlyhighrangedollar = usagerangedata.get(i).getHighRange()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        if (globalvariables!!.haveNetworkConnection(activity)) {
                            // loadlastyeardata();
                            //GetThreeModuleDataTask task = new GetThreeModuleDataTask();
                            power = false
                            gas = true
                            water = false
                            val task = callDollarKWHWebServiceTask()
                            task.applicationContext = activity
                            task.execute()
                        } else {
                            if (progressdialog != null) progressdialog!!.cancel()
                            globalvariables!!.Networkalertmessage(activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                if (progressdialog != null) progressdialog!!.cancel()
                e.printStackTrace()
            }

        }
    }

    private fun setGasUsageData() {
        try {
            var expectedbudget = ""
            if (gasexpectedusagesdata != null && gasexpectedusagesdata!!.size > 0) {
                //  for (int i = 0; i < gasexpectedusagesdata.size(); i++) {

                val co2ExpectedUsage = gasexpectedusagesdata!![gasexpectedusagesdata!!.size - 1].getCo2ExpectedUsage()
                if (dollar!!) {
                    val co2UsageSoFar =
                        isConvertTODecimal(gasexpectedusagesdata!![gasexpectedusagesdata!!.size - 1].getCo2UsageSoFar())
                    tv_usagesofarvalue!!.text =
                        "$" + CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption()
                    expectedbudget = mStrUnit + isConvertTODecimal(co2ExpectedUsage)
                } else if (kwh!!) {
                    val co2unitusagesofar =
                        gasexpectedusagesdata!![gasexpectedusagesdata!!.size - 1].getCo2unitusagesofar()

                    if (power!!)
                        tv_usagesofarvalue!!.setText(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption() + " kWh")
                    else if (gas!!)
                        tv_usagesofarvalue!!.setText(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption() + " CCF")

                    //tv_usagesofarvalue.setText(isConvertTODecimal(co2unitusagesofar) + mStrUnit);
                    val co2unitexpectedusage =
                        gasexpectedusagesdata!![gasexpectedusagesdata!!.size - 1].getCo2unitexpectedusage()
                    expectedbudget = isConvertTODecimal(co2unitexpectedusage) + mStrUnit
                }

                try {
                    tv_expectedusagevalue!!.text = isConvertTODecimal(expectedbudget)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // POWER/WATER/GAS====================DOLLAR/KWH===============Asynktask
    private inner class callDollarKWHWebServiceTask : AsyncTask<Void, Void, String>() {
        var applicationContext: Context? = null

        override fun onPreExecute() {

            try {
                if (progressdialog != null) {
                    if (!progressdialog!!.isShowing)
                        progressdialog = ProgressDialog.show(
                            applicationContext,
                            null,
                            DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                        )
                } else {
                    progressdialog = ProgressDialog.show(
                        applicationContext,
                        null,
                        DBNew!!.getLabelText("ML_Others_Span_Pleasewait…", languageCode)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg params: Void): String {

            try {
                val accountnumber = sharedpref!!.loadPreferences(Constant.DEFAULTACCOUNTNUMBER)

                if (power_tab!!)
                    CompareMeResultPower = WebServicesPost.loadUsageMonthlyCompare(
                        accountnumber,
                        "1",
                        "",
                        "M",
                        sharedpref!!.loadPreferences(Constant.LoginToken),
                        meternumber,
                        single_meter
                    )

                if (gas_tab!!)
                    CompareMeResultGas = WebServicesPost.loadGasUsageMonthlyCompare(
                        accountnumber,
                        "",
                        "M",
                        sharedpref!!.loadPreferences(Constant.LoginToken),
                        meternumber,
                        single_meter
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        override fun onPostExecute(result: String) {

            super.onPostExecute(result)
            try {
                if (progressdialog != null) progressdialog!!.cancel()
                if (power_tab!!)
                    CompareMeResult = CompareMeResultPower
                if (gas_tab!!)
                    CompareMeResult = CompareMeResultGas

                if (CompareMeResult != null && !CompareMeResult!!.equals(
                        "",
                        ignoreCase = true
                    ) && CompareMeResult!!.length > 0
                ) {

                    val cskwhhandler = New_Compare_Handler()
                    cskwhhandler.setParserObjIntoObj(CompareMeResult, "Monthly")

                    Collections.reverse(cskwhhandler.fetchcurrentdata())
                    Collections.reverse(cskwhhandler.fetchpreviousdata())

                    CS_kwh_currentdata = cskwhhandler.fetchcurrentdata()
                    CS_kwh_previousdata = cskwhhandler.fetchpreviousdata()

                    selectedmonth = CS_kwh_currentdata!!.size - 1

                    println("CS_kwh_currentdata size : " + CS_kwh_currentdata!!.size)
                    println("CS_kwh_previousdata size : " + CS_kwh_previousdata!!.size)

                    if (CS_kwh_currentdata!!.size > 0) {
                        tv_selectedmonth.text =
                            setMonth(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getMonth()) + ", " + CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getYear()
                    }
                }

                // Message Second
                if (CS_kwh_currentdata != null && CS_kwh_currentdata!!.size > 0) {
                    val task = CompareMeTask()
                    task.applicationContext = activity
                    task.execute("" + CS_kwh_currentdata!!.size)
                } else
                    globalvariables!!.showAlert(
                        activity,
                        DBNew!!.getLabelText(getString(R.string.Common_Message), languageCode),
                        DBNew!!.getLabelText(getString(R.string.Common_NoDataDisplay), languageCode),
                        1,
                        DBNew!!.getLabelText(getString(R.string.Common_OK), languageCode),
                        ""
                    )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    //set color  to variable from service
    private fun setColorDataset() {
        try {
            val CURRENT = "Current"
            val PREVIOUS = "Previous"
            val UTILITY = "Utility"
            val ZIP = "Zip"

            val arrayChartColorDataSet = commonSingleton!!.getArrayChartColorDataSet()
            //((BaseChartActivity) getActivity()).getArrayChartColorDataSet();
            if (commonSingleton!!.validateArrayList(arrayChartColorDataSet)) {
                for (i in arrayChartColorDataSet.indices) {
                    val chartColorDataset = arrayChartColorDataSet.get(i) as ChartColorDataset

                    if (chartColorDataset.getConfigOption().equalsIgnoreCase(CURRENT)) {
                        currentColor = chartColorDataset.getConfigValue()
                    }

                    if (chartColorDataset.getConfigOption().equalsIgnoreCase(PREVIOUS)) {
                        previousColor = chartColorDataset.getConfigValue()
                    }

                    if (chartColorDataset.getConfigOption().equalsIgnoreCase(UTILITY)) {
                        utilityColor = chartColorDataset.getConfigValue()
                    }

                    if (chartColorDataset.getConfigOption().equalsIgnoreCase(ZIP)) {
                        zipColor = chartColorDataset.getConfigValue()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showcomparelegends(
        currentmonthyear: String, currentvalue: String, previousmonthyear: String,
        previousvalue: String, zipmonthyear: String, zipvalue: String, utilitymonthyear: String, utilityvalue: String
    ) {
        try {
            comparelegendlayout1!!.removeAllViews()

            val layoutparams: RelativeLayout.LayoutParams? = null

            val monthyeararray = ArrayList<String>()
            val legendcolorarray = ArrayList<Int>()
            val valuearray = ArrayList<String>()
            val rangecolorarray = ArrayList<Int>()

            if (isKwh_Dollar_Gallon == 1) { //kwh

                if (!currentmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(currentmonthyear)
                    legendcolorarray.add(Color.parseColor(currentColor))
                    //    legendcolorarray.add(Color.parseColor(getResources().getString(R.color.currentcolor)));
                    if (power!!) {
                        valuearray.add(isConvertTODecimal(currentvalue) + " kWh")
                    } else if (gas!!) {
                        if (gas!! && isKwh_Dollar_Gallon == 2)
                            valuearray.add("$" + isConvertTODecimal(currentvalue))
                        else
                            valuearray.add(isConvertTODecimal(currentvalue) + " CCF")
                    }
                    rangecolorarray.add(rangecolorvalue(currentvalue))
                }

                if (!previousmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(previousmonthyear)
                    legendcolorarray.add(Color.parseColor(previousColor))
                    if (power!!) {
                        valuearray.add(isConvertTODecimal(previousvalue) + " kWh")
                    } else if (gas!!) {
                        //valuearray.add(isConvertTODecimal(previousvalue) + " CCF");
                        if (gas!! && isKwh_Dollar_Gallon == 2)
                            valuearray.add("$" + isConvertTODecimal(previousvalue))
                        else
                            valuearray.add(isConvertTODecimal(previousvalue) + " CCF")
                    }
                    rangecolorarray.add(rangecolorvalue(previousvalue))
                }

                if (!zipmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(zipmonthyear)
                    legendcolorarray.add(Color.parseColor(zipColor))
                    //  legendcolorarray.add(Color.parseColor(getResources().getString(R.color.zipcolor)));
                    if (power!!) {
                        valuearray.add(isConvertTODecimal(zipvalue) + " kWh")
                    } else if (gas!!) {
                        valuearray.add(isConvertTODecimal(zipvalue) + " CCF")
                    }
                    rangecolorarray.add(rangecolorvalue(zipvalue))
                }

                if (!utilitymonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(utilitymonthyear)
                    legendcolorarray.add(Color.parseColor(utilityColor))
                    //   legendcolorarray.add(Color.parseColor(getResources().getString(R.color.utilitycolor)));
                    if (power!!) {
                        valuearray.add("$utilityvalue kWh")
                    } else if (gas!!) {
                        valuearray.add("$utilityvalue CCF")
                    }
                    rangecolorarray.add(rangecolorvalue(utilityvalue))
                }
            } else if (isKwh_Dollar_Gallon == 2) { //dollar

                if (!currentmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(currentmonthyear)
                    legendcolorarray.add(Color.parseColor(currentColor))
                    // legendcolorarray.add(Color.parseColor(getResources().getString(R.color.currentcolor)));
                    valuearray.add("$" + isConvertTODecimal(currentvalue))
                    rangecolorarray.add(rangecolorvalue(currentvalue))
                }

                if (!previousmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(previousmonthyear)
                    legendcolorarray.add(Color.parseColor(previousColor))
                    //  legendcolorarray.add(Color.parseColor(getResources().getString(R.color.previouscolor)));
                    valuearray.add("$" + isConvertTODecimal(previousvalue))
                    rangecolorarray.add(rangecolorvalue(previousvalue))
                }

                if (!zipmonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(zipmonthyear)
                    legendcolorarray.add(Color.parseColor(zipColor))
                    valuearray.add("$" + isConvertTODecimal(zipvalue))
                    rangecolorarray.add(rangecolorvalue(zipvalue))
                }

                if (!utilitymonthyear.equals("", ignoreCase = true)) {
                    monthyeararray.add(utilitymonthyear)
                    legendcolorarray.add(Color.parseColor(utilityColor))
                    valuearray.add("$" + isConvertTODecimal(utilityvalue))
                    rangecolorarray.add(rangecolorvalue(utilityvalue))
                }
            }

            val llayout = LinearLayout(activity)
            llayout.orientation = LinearLayout.HORIZONTAL
            llayout.weightSum = 1f
            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            llayout.gravity = Gravity.CENTER
            llayout.layoutParams = params

            for (i in monthyeararray.indices) {

                val llout = LinearLayout(activity)
                llout.orientation = LinearLayout.VERTICAL
                val paramsll = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                paramsll.weight = 1f / monthyeararray.size
                paramsll.setMargins(5, 0, 0, 0)
                llout.gravity = Gravity.CENTER_HORIZONTAL
                llout.layoutParams = paramsll

                val rangevalue =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val textview1 = TextView(activity)
                textview1.layoutParams = rangevalue
                textview1.setTextColor(legendcolorarray[i])
                textview1.setTypeface(null, Typeface.BOLD)
                textview1.setPadding(5, 0, 5, 0)
                textview1.textSize = resources.getDimensionPixelSize(R.dimen.comparespending_legendvalue).toFloat()
                textview1.gravity = Gravity.CENTER or Gravity.TOP
                textview1.text = "" + valuearray[i]

                val paramstv =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val textview = TextView(activity)
                textview.layoutParams = paramstv
                textview.setPadding(5, 0, 5, 0)
                textview.setTextColor(resources.getColor(R.color.usage_light_gray))
                textview.textSize =
                    resources.getDimensionPixelSize(R.dimen.comparespending_legendvalue_Bottom).toFloat()
                textview.gravity = Gravity.CENTER or Gravity.BOTTOM
                textview.text = monthyeararray[i] + " " // add colon here if not found in legends

                llout.addView(textview1)
                llout.setBackgroundColor(Color.WHITE)
                llout.addView(textview)

                llayout.addView(llout)
            }
            comparelegendlayout1!!.addView(llayout)

        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }

    fun rangecolorvalue(value: String): Int {
        var colorvalue = 0

        try {
            val usagevalue = java.lang.Double.parseDouble(value)

            if (isKwh_Dollar_Gallon == 1) {
                if (usagevalue <= monthlylowrangekwh) {
                    colorvalue = Color.rgb(1, 128, 0)
                } else if (usagevalue > monthlylowrangekwh && usagevalue <= monthlyhighrangekwh) {
                    colorvalue = Color.rgb(247, 207, 19)
                } else if (usagevalue > monthlyhighrangekwh) {
                    colorvalue = Color.rgb(199, 0, 15)
                }
            } else if (isKwh_Dollar_Gallon == 2) {
                if (usagevalue <= monthlylowrangedollar) {
                    colorvalue = Color.rgb(1, 128, 0)
                } else if (usagevalue > monthlylowrangedollar && usagevalue <= monthlyhighrangedollar) {
                    colorvalue = Color.rgb(247, 207, 19)
                } else if (usagevalue > monthlyhighrangedollar) {
                    colorvalue = Color.rgb(199, 0, 15)
                }
            } else if (isKwh_Dollar_Gallon == 3) {
                if (usagevalue <= monthlylowrangegallon) {
                    colorvalue = Color.rgb(1, 128, 0)
                } else if (usagevalue > monthlylowrangegallon && usagevalue <= monthlyhighrangegallon) {
                    colorvalue = Color.rgb(247, 207, 19)
                } else if (usagevalue > monthlyhighrangegallon) {
                    colorvalue = Color.rgb(199, 0, 15)
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        return colorvalue
    }

    //Draw Line Chart for Compare All for both Kwh & dollar
    internal fun compareMeLineChartData(
        currentvalues: List<Float>, previousvalues: List<Float>, allocationvalues: List<Float>,
        xVals: ArrayList<String>, ylabel: String, colorSet1: String, colorSet2: String
    ): LineData? {

        var d: LineData? = null
        try {
            var f: Float?
            //            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
            val dataSets = ArrayList<ILineDataSet>()
            // ----first line------------------------------------------------------
            val floatArray = FloatArray(currentvalues.size)
            for (i in currentvalues.indices) {
                f = currentvalues[i]
                floatArray[i] = f ?: java.lang.Float.NaN
            }

            val yVals1 = ArrayList<Entry>()
            for (i in floatArray.indices) {
                if (floatArray[i] > 0) {
                    val usagevalue = floatArray[i]

                    if (usagevalue > 0.0)
                        yVals1.add(Entry(usagevalue, i.toFloat()))
                } else
                    yVals1.add(Entry(-1f, i.toFloat()))
            }

            val set1 = LineDataSet(yVals1, "")
            set1.setDrawValues(true)
            set1.valueTextSize = 10f
            set1.color = Color.parseColor(colorSet1)
            set1.valueTextColor = Color.parseColor(colorSet1)
            set1.lineWidth = 3f

            /// ---second
            /// line------------------------------------------------------------
            val floatArray2 = FloatArray(previousvalues.size)
            for (i in previousvalues.indices) {
                f = previousvalues[i]
                floatArray2[i] = f ?: java.lang.Float.NaN
            }

            val yVals2 = ArrayList<Entry>()
            for (i in floatArray2.indices) {
                if (floatArray2[i] > 0) {
                    val usagevalue2 = floatArray2[i]

                    if (usagevalue2 > 0.0)
                        yVals2.add(BarEntry(usagevalue2, i.toFloat()))
                } else
                    yVals2.add(BarEntry(-1f, i.toFloat()))
            }

            val set2 = LineDataSet(yVals2, ylabel)
            set2.color = Color.parseColor(colorSet2)
            set2.setDrawValues(true)
            // set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.valueTextColor = Color.parseColor(colorSet2)
            set2.lineWidth = 3f

            set1.axisDependency = YAxis.AxisDependency.LEFT
            set2.axisDependency = YAxis.AxisDependency.LEFT

            val df = DecimalFormat("#0.00")

            if (water_HCF!! || water_gallon!!) {
                val yVals3 = ArrayList<Entry>()
                val floatArray3 = FloatArray(allocationvalues.size)

                for (i in allocationvalues.indices) {
                    f = allocationvalues[i]
                    floatArray3[i] = f ?: java.lang.Float.NaN // Or whatever default you want.
                }

                for (i in floatArray3.indices) {
                    if (floatArray3[i] > 0) {
                        val usagevalue =
                            java.lang.Float.parseFloat(df.format(java.lang.Double.parseDouble(allocationvalues[i].toString())))

                        if (usagevalue > 0.0)
                            yVals3.add(Entry(usagevalue, i.toFloat()))
                    } else
                        yVals3.add(Entry(-1f, i.toFloat()))
                }

                val set = LineDataSet(yVals3, ylabel)
                set.color = resources.getColor(R.color.apptheme_primary_color)
                set.lineWidth = 3f
                set.setCircleColor(resources.getColor(R.color.apptheme_primary_color))
                set.setCircleColorHole(resources.getColor(R.color.apptheme_primary_color))
                set.circleSize = 3f

                set.fillColor = resources.getColor(R.color.apptheme_primary_color)
                //set.setDrawCubic(true);
                set.setDrawValues(true)
                set.valueTextSize = 0f
                set.valueTextColor = resources.getColor(R.color.apptheme_primary_color)
                set.valueFormatter = MyValueFormatter()
                set.axisDependency = YAxis.AxisDependency.LEFT
                dataSets.add(set)
            }

            dataSets.add(set1)
            dataSets.add(set2)
            d = LineData(xVals, dataSets)

            d.setValueTextSize(10f)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        return d
    }

    //Draw Bar Chart for Compare All for both Kwh & dollar

    internal fun compareMeBarChartData(
        currentvalues: List<Float>, previousvalues: List<Float>,
        xVals: ArrayList<String>, ylabel: String, colorSet1: String, colorSet2: String
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

            val yVals1 = ArrayList<BarEntry>()
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

            val yVals2 = ArrayList<BarEntry>()
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

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            dataSets.add(set2)

            d = BarData(xVals, dataSets)
            d.setGroupSpace(80f)
            d.setValueTextSize(10f)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return d
    }

    fun compare_me_selected() {

        tv_disclaimer.text = Html.fromHtml(
            "<font color='#ff0000'>" + DBNew!!.getLabelText(
                getString(R.string.Compare_Disclaimer),
                languageCode
            ) + ": " + "</font>" + DBNew!!.getLabelText(getString(R.string.Compare_CompareMeenablesto), languageCode)
        )
        (activity as CompareSpending_Screen).checkTextviewEllipsized(
            tv_disclaimer,
            tv_read_more,
            DBNew!!.getLabelText(getString(R.string.Compare_CompareMeenablesto), languageCode)
        )

        try {
            tv_compareme.setBackgroundColor(resources.getColor(R.color.apptheme_primary_color))
            tv_comparezip.setBackgroundColor(resources.getColor(R.color.apptheme_background))
            tv_compareutility.setBackgroundColor(resources.getColor(R.color.apptheme_background))
            tv_compareall.setBackgroundColor(resources.getColor(R.color.apptheme_background))

            tv_comparezip.setTextColor(resources.getColor(R.color.apptheme_color_subheading))
            tv_compareutility.setTextColor(resources.getColor(R.color.apptheme_color_subheading))
            tv_compareall.setTextColor(resources.getColor(R.color.apptheme_color_subheading))
            tv_compareme.setTextColor(resources.getColor(R.color.white))

        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }

    fun MonthSelection_dialog() {

        try {
            if (monthslist.size > 0) {
                months = monthslist.toTypedArray()

                val builder = AlertDialog.Builder(activity)
                builder.setTitle(DBNew!!.getLabelText(getString(R.string.Compare_Month), languageCode))

                builder.setSingleChoiceItems(
                    months, selectedmonth
                ) { dialog, id ->
                    selectedmonth = id

                    tv_selectedmonth.text = months!![selectedmonth].toString()
                    isMonthSelected = true
                    var currentrangevalue = ""

                    var previousmonthyear = ""
                    var previousrangevalue = ""
                    previousmonthyear = ""

                    */
/*  compareSpendingLineChart.setDrawBarShadow(false);*//*

                    var currentmonthyear = ""
                    if (isKwh_Dollar_Gallon == 2) {
                        */
/*currentrangevalue = CS_kwh_currentdata.get(selectedmonth).getTotalConsumptionAmount();
                                    currentmonthyear = CS_kwh_currentdata.get(selectedmonth).getYear();*//*


                        for (i in CS_kwh_currentdata!!.indices) {
                            if (!IsDollar) {
                                if (!CS_kwh_currentdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                    currentrangevalue = CS_kwh_currentdata!![selectedmonth].getTotalConsumption()
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![selectedmonth].getMonth()) + ", " + CS_kwh_currentdata!![selectedmonth].getYear()
                                }
                            } else if (IsDollar) {
                                if (!CS_kwh_currentdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                    currentrangevalue = CS_kwh_currentdata!![selectedmonth].getTotalConsumptionAmount()
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![selectedmonth].getMonth()) + ", " + CS_kwh_currentdata!![selectedmonth].getYear()
                                }
                            }
                        }
                    } else if (isKwh_Dollar_Gallon == 1) {
                        for (i in CS_kwh_currentdata!!.indices) {
                            if (!IsDollar) {
                                if (!CS_kwh_currentdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                    currentrangevalue = CS_kwh_currentdata!![selectedmonth].getTotalConsumption()
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![selectedmonth].getMonth()) + ", " + CS_kwh_currentdata!![selectedmonth].getYear()
                                }
                            } else if (IsDollar) {
                                if (!CS_kwh_currentdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                    currentrangevalue = CS_kwh_currentdata!![selectedmonth].getTotalConsumptionAmount()
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![selectedmonth].getMonth()) + ", " + CS_kwh_currentdata!![selectedmonth].getYear()
                                }
                            }
                        }
                    }

                    if (isMe_Zip_Utility_All == 0) {

                        if (isKwh_Dollar_Gallon == 2) {
                            //previousrangevalue = CS_dollar_previousdata.get(selectedmonth).getTotalConsumption();
                            //previousmonthyear = CS_dollar_previousdata.get(selectedmonth).getYear();

                            for (i in CS_kwh_previousdata!!.indices) {
                                if (!IsDollar) {
                                    if (!CS_kwh_previousdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                        previousrangevalue = CS_kwh_previousdata!![selectedmonth].getTotalConsumption()
                                        previousmonthyear =
                                            setMonth(CS_kwh_previousdata!![selectedmonth].getMonth()) + ", " + CS_kwh_previousdata!![selectedmonth].getYear()
                                    }
                                } else if (IsDollar) {
                                    if (!CS_kwh_previousdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                        previousrangevalue =
                                            CS_kwh_previousdata!![selectedmonth].getTotalConsumptionAmount()
                                        previousmonthyear =
                                            setMonth(CS_kwh_previousdata!![selectedmonth].getMonth()) + ", " + CS_kwh_previousdata!![selectedmonth].getYear()
                                    }
                                }
                            }
                        } else if (isKwh_Dollar_Gallon == 1) {
                            for (i in CS_kwh_previousdata!!.indices) {
                                if (!IsDollar) {
                                    if (!CS_kwh_previousdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                        previousrangevalue = CS_kwh_previousdata!![selectedmonth].getTotalConsumption()
                                        previousmonthyear =
                                            setMonth(CS_kwh_previousdata!![selectedmonth].getMonth()) + ", " + CS_kwh_previousdata!![selectedmonth].getYear()
                                    }
                                } else if (IsDollar) {
                                    if (!CS_kwh_previousdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                        previousrangevalue =
                                            CS_kwh_previousdata!![selectedmonth].getTotalConsumptionAmount()
                                        previousmonthyear =
                                            setMonth(CS_kwh_previousdata!![selectedmonth].getMonth()) + ", " + CS_kwh_previousdata!![selectedmonth].getYear()
                                    }
                                }
                            }
                        }
                        showcomparelegends(
                            currentmonthyear, currentrangevalue, previousmonthyear, previousrangevalue, "",
                            "", "", ""
                        )
                    }

                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dollar_selected() {

        try {
            btn_dollar.setBackgroundResource(R.drawable.leftfilledshape)
            btn_kwh.setBackgroundResource(R.drawable.rightblankshape)

            btn_gallon.setBackgroundResource(R.drawable.rightblankshape)

            if (power!!)
                btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewkWh), languageCode))
            else if (gas!!)
                btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewCCF), languageCode))

            btn_dollar.setTextColor(resources.getColor(R.color.white))
            btn_kwh.setTextColor(resources.getColor(R.color.apptheme_primary_color))
            btn_gallon.setTextColor(resources.getColor(R.color.apptheme_primary_color))
            // Message First
            if (CS_kwh_currentdata != null && CS_kwh_currentdata!!.size > 0) {
                val task = CompareMeTask()
                task.applicationContext = activity
                task.execute("" + CS_kwh_currentdata!!.size)
            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }

    fun kwh_selected() {

        try {
            btn_dollar.setBackgroundResource(R.drawable.leftblankshape)
            btn_kwh.setBackgroundResource(R.drawable.rightfilledshape)

            isKwh_Dollar_Gallon = 1

            btn_dollar.setTextColor(resources.getColor(R.color.apptheme_primary_color))
            btn_kwh.setTextColor(resources.getColor(R.color.white))
            btn_gallon.setTextColor(resources.getColor(R.color.apptheme_primary_color))

            if (CS_kwh_currentdata != null && CS_kwh_currentdata!!.size > 0) {
                val task = CompareMeTask()
                task.applicationContext = activity
                task.execute("" + CS_kwh_currentdata!!.size)
            } else
                globalvariables!!.showAlert(
                    activity,
                    DBNew!!.getLabelText(getString(R.string.Common_Message), languageCode),
                    DBNew!!.getLabelText(getString(R.string.Common_NoDataDisplay), languageCode),
                    1,
                    DBNew!!.getLabelText(getString(R.string.Common_OK), languageCode),
                    ""
                )

        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupFullviewGraphview(false)
    }

    private fun setupFullviewGraphview(isCallFromOnCreate: Boolean?) {

        try {
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                try {
                    val params = li_fragmentlayout!!.layoutParams
                    params.height = Constant.getPixels(
                        TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.usagechart_landscape_height)
                    )
                    li_fragmentlayout!!.layoutParams = params

                    if (viewtype.equals(
                            DBNew!!.getLabelText(getString(R.string.Compare_FullView), languageCode),
                            ignoreCase = true
                        )
                    ) {

                    } else if (viewtype.equals(
                            DBNew!!.getLabelText(
                                getString(R.string.Compare_GraphView),
                                languageCode
                            ), ignoreCase = true
                        )
                    ) {
                        // ll_top_buttons.setVisibility(View.GONE);
                        rel_monthselection!!.visibility = View.GONE
                        li_buttonslayout.visibility = View.GONE
                        cv_usagestats!!.visibility = View.GONE
                        ll_projected_usage!!.visibility = View.GONE
                    }

                    hideSmmmaryView()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }

                changeViewParent(scrollCompanion, scroll)
            } else if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if ((!isCallFromOnCreate)!!) {
                    val params = li_fragmentlayout!!.layoutParams
                    try {
                        params.height = -1
                        li_fragmentlayout!!.layoutParams = params
                        hideSmmmaryView()
                        li_buttonslayout.visibility = View.VISIBLE
                        //   ll_top_buttons.setVisibility(View.VISIBLE);
                        rel_monthselection!!.visibility = View.VISIBLE
                        li_buttonslayout.visibility = View.VISIBLE
                        cv_usagestats!!.visibility = View.VISIBLE
                        ll_projected_usage!!.visibility = View.GONE
                    } catch (e: Resources.NotFoundException) {
                        e.printStackTrace()
                    }

                    changeViewParent(scroll, scrollCompanion)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun hideSmmmaryView() {
        try {
            if (summary!!) {
                cv_usagestats!!.visibility = View.VISIBLE
            } else {
                cv_usagestats!!.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setHideShow() {

        try {
            power_dollar = DBNew!!.getFeatureShowStatus("Power.$")
            water_dollar = DBNew!!.getFeatureShowStatus("Water.$")
            gas_dollar = DBNew!!.getFeatureShowStatus("Gas.$")

            compare_all = DBNew!!.getFeatureShowStatus("Compare.All")
            gas_CCF = DBNew!!.getFeatureShowStatus("Gas.CCF")
            compare_me = DBNew!!.getFeatureShowStatus("Compare.Me")

            power_kwh = DBNew!!.getFeatureShowStatus("Power.kWh")
            project_usage = DBNew!!.getFeatureShowStatus("ProjectUsage")
            summary = DBNew!!.getFeatureShowStatus("Compare.Summary")

            compare_utility = DBNew!!.getFeatureShowStatus("Compare.Utility")
            water_HCF = DBNew!!.getFeatureShowStatus("Water.HCF")
            compare_zip = DBNew!!.getFeatureShowStatus("Compare.Zip")

            water_gallon = DBNew!!.getFeatureShowStatus("Water.GAL")
            */
/*power_dollar = true;
            power_kwh = true;
            gas_dollar = true;
            gas_CCF = true;*//*


        } catch (e: Exception) {
            e.printStackTrace()
        }

        */
/*if (globalvariables.PAYMENTCONFIG == 0) {
            power_dollar = false;
            water_dollar = false;
            gas_dollar = false;
        }*//*

        try {
            globalvariables!!.compare_me = compare_me
            globalvariables!!.compare_zip = compare_zip
            globalvariables!!.compare_utility = compare_utility
            globalvariables!!.compare_all = compare_all
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (project_usage!!)
                ll_projected_usage!!.visibility = View.VISIBLE
            else
                ll_projected_usage!!.visibility = View.GONE

            hideSmmmaryView()

            if ((!compare_me)!!) { // hide Radio Button Compare Me
                val layout = tv_compareme.parent
                (layout as ViewGroup).removeView(tv_compareme)
            } else {
                tv_compareme.visibility = View.GONE // visible Radio Button Compare Me
            }
            if ((!compare_zip)!!) { // hide Radio Button Compare Zip
                val layout = tv_comparezip.parent
                (layout as ViewGroup).removeView(tv_comparezip)
            } else {
                tv_comparezip.visibility = View.VISIBLE// visible Radio Button Compare Zip
            }
            if ((!compare_utility)!!) {// hide Radio Button Compare Zip
                val layout = tv_compareutility.parent
                (layout as ViewGroup).removeView(tv_compareutility)
            } else {
                tv_compareutility.visibility = View.VISIBLE// visible Radio Button Compare Utility
            }
            if ((!compare_all)!!) {// hide Radio Button Compare All
                val layout = tv_compareall.parent
                (layout as ViewGroup).removeView(tv_compareall)
            } else {
                tv_compareall.visibility = View.VISIBLE// visible Radio Button Compare All
            }
            if ((!compare_me)!!) {
                if ((!compare_zip)!!) {
                    if ((!compare_utility)!!) {
                        isMe_Zip_Utility_All = 3
                    } else {
                        isMe_Zip_Utility_All = 2
                    }
                } else {
                    isMe_Zip_Utility_All = 1
                }
            } else {
                isMe_Zip_Utility_All = 0
                compare_me_selected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun hideshowbuttons() {
        try {
            if (power_tab!!) {
                power = true
                water = false
                gas = false

                if (globalvariables!!.haveNetworkConnection(activity)) {
                    val task = PowerWebServiceTask()
                    task.applicationContext = activity
                    task.execute()
                } else {
                    if (progressdialog != null) progressdialog!!.cancel()
                    globalvariables!!.Networkalertmessage(activity)
                }

                btn_gallon.visibility = View.GONE

                if (power_dollar!! && power_kwh!!) {
                    isKwh_Dollar_Gallon = 2
                    dollar_selected()
                    btn_dollar.performClick()
                } else if ((!power_dollar)!! && power_kwh!!) {
                    isKwh_Dollar_Gallon = 1
                    btn_kwh.setBackgroundDrawable(resources.getDrawable(R.drawable.roundfilledshape))
                    btn_dollar.visibility = View.VISIBLE
                    btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewkWh), languageCode))
                    btn_kwh.setTextColor(resources.getColor(R.color.usagebutton_selected_textcolor))
                    btn_kwh.performClick()
                    */
/*     hide top tab layout for single tab*//*

                    ll_top_buttons.visibility = View.VISIBLE
                } else if (power_dollar!! && (!power_kwh)!!) {
                    isKwh_Dollar_Gallon = 2
                    btn_dollar.performClick()
                    btn_dollar.setBackgroundDrawable(resources.getDrawable(R.drawable.roundfilledshape))
                    btn_dollar.setTextColor(resources.getColor(R.color.usagebutton_selected_textcolor))
                    btn_kwh.visibility = View.GONE
                    */
/*     hide top tab layout for single tab*//*

                    ll_top_buttons.visibility = View.VISIBLE
                } else if ((!power_dollar)!! && (!power_kwh)!!) {
                    isAllModuleHide = true
                    try {
                        scroll.visibility = View.GONE
                        scrollCompanion.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else if (gas_tab!!) {
                power = false
                water = false
                gas = true
                btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewHCF), languageCode))
                if (globalvariables!!.haveNetworkConnection(activity)) {
                    val task = GasWebServiceTask()
                    task.applicationContext = activity
                    task.execute()
                } else {
                    if (progressdialog != null) progressdialog!!.cancel()
                    globalvariables!!.Networkalertmessage(activity)
                }
                btn_gallon.visibility = View.GONE

                if (gas_dollar!! && gas_CCF!!) {
                    isKwh_Dollar_Gallon = 2

                    dollar_selected()
                    btn_dollar.performClick()
                } else if ((!gas_dollar)!! && gas_CCF!!) {
                    isKwh_Dollar_Gallon = 1
                    btn_kwh.setBackgroundDrawable(resources.getDrawable(R.drawable.roundfilledshape))
                    btn_dollar.visibility = View.VISIBLE
                    btn_kwh.setText(DBNew!!.getLabelText(getString(R.string.Compare_ViewCCF), languageCode))

                    btn_kwh.performClick()

                    btn_kwh.setTextColor(resources.getColor(R.color.usagebutton_selected_textcolor))
                    */
/* hide top tab layout for single tab*//*

                    ll_top_buttons.visibility = View.VISIBLE
                } else if (gas_dollar!! && (!gas_CCF)!!) {
                    isKwh_Dollar_Gallon = 2
                    btn_dollar.performClick()
                    btn_dollar.setBackgroundDrawable(resources.getDrawable(R.drawable.roundfilledshape))
                    btn_dollar.setTextColor(resources.getColor(R.color.usagebutton_selected_textcolor))

                    btn_kwh.visibility = View.GONE

                    */
/*     hide top tab layout for single tab*//*

                    ll_top_buttons.visibility = View.VISIBLE
                } else if ((!gas_dollar)!! && (!gas_CCF)!!) {

                    isAllModuleHide = true
                    try {
                        scroll.visibility = View.GONE
                        scrollCompanion.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }

    // function to use multilingual for month
    fun setMonth(month: String): String {
        var month = month

        try {
            if (month.contains("-"))
                return month

            if (month.contains("Jan")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jan), languageCode)
            } else if (month.contains("Feb")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Feb), languageCode)
            } else if (month.contains("Mar")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Mar), languageCode)
            } else if (month.contains("Apr")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Apr), languageCode)
            } else if (month.contains("May")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_May), languageCode)
            } else if (month.contains("Jun")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jun), languageCode)
            } else if (month.contains("Jul")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jul), languageCode)
            } else if (month.contains("Aug")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Aug), languageCode)
            } else if (month.contains("Sep")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Sep), languageCode)
            } else if (month.contains("Oct")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Oct), languageCode)
            } else if (month.contains("Nov")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Nov), languageCode)
            } else if (month.contains("Dec")) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Dec), languageCode)
            }

            if (month.equals("01", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jan), languageCode)
            } else if (month.equals("02", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Feb), languageCode)
            } else if (month.equals("03", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Mar), languageCode)
            } else if (month.equals("04", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Apr), languageCode)
            } else if (month.equals("05", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_May), languageCode)
            } else if (month.equals("06", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jun), languageCode)
            } else if (month.equals("07", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Jul), languageCode)
            } else if (month.equals("08", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Aug), languageCode)
            } else if (month.equals("09", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Sep), languageCode)
            } else if (month.equals("10", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Oct), languageCode)
            } else if (month.equals("11", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Nov), languageCode)
            } else if (month.equals("12", ignoreCase = true)) {
                month = DBNew!!.getLabelText(getString(R.string.Compare_Dec), languageCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return month
    }

    private fun isConvertTODecimal(string: String): String {
        var returnString = ""
        val mFormat = DecimalFormat("#0.00")
        try {
            if (isDecimal!!)
                returnString = mFormat.format(java.lang.Double.parseDouble(string))
            else
                returnString = Math.round(java.lang.Double.parseDouble(string)).toString()
            return returnString
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return string
    }

    ///////////////////////////////////////////////////////////////////////////////////////DONEE
    //POWER compareme kWh task
    private inner class CompareMeTask : AsyncTask<String, Void, Int>() {

        var applicationContext: Context? = null
        internal var monthnumber = 0

        override fun onPreExecute() {
            //this.progressdialog = ProgressDialog.show(applicationContext, ConstantMessage.LOADING_DATA, ConstantMessage.MSG_WAIT, true);
        }

        override fun doInBackground(vararg params: String): Int? {

            try {
                if (!params[0].equals("", ignoreCase = true))
                    monthnumber = Integer.parseInt(params[0])
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            return monthnumber
        }

        override fun onPostExecute(result: Int?) {

            super.onPostExecute(result)
            try {
                li_fragmentlayout!!.removeAllViews()

                compareSpendingLineChart!!.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                compareSpendingLineChart!!.setNoDataText(DBNew!!.getLabelText("ML_Msg_NoData", languageCode))
                li_fragmentlayout!!.addView(compareSpendingLineChart)

                if (CS_kwh_currentdata!!.size > 0 && CS_kwh_previousdata!!.size > 0) {
                    val values = ArrayList<DoubleArray>()

                    val currentarray = DoubleArray(CS_kwh_currentdata!!.size)
                    val previousarray = DoubleArray(CS_kwh_previousdata!!.size)
                    val currentvalues = ArrayList<Float>()
                    val previousvalues = ArrayList<Float>()

                    for (i in CS_kwh_currentdata!!.indices) {
                        if (!IsDollar) {
                            if (!CS_kwh_currentdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                currentvalues.add(java.lang.Float.parseFloat(isConvertTODecimal(CS_kwh_currentdata!![i].getTotalConsumption())))
                                currentarray[i] =
                                    java.lang.Double.parseDouble(isConvertTODecimal(CS_kwh_currentdata!![i].getTotalConsumption()))
                                System.out.println("current value for " + CS_kwh_currentdata!![i].getMonth() + "," + CS_kwh_currentdata!![i].getYear() + ": " + CS_kwh_currentdata!![i].getTotalConsumption())
                            }
                        } else if (IsDollar) {
                            if (!CS_kwh_currentdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                currentvalues.add(java.lang.Float.parseFloat(isConvertTODecimal(CS_kwh_currentdata!![i].getTotalConsumptionAmount())))
                                currentarray[i] =
                                    java.lang.Double.parseDouble(isConvertTODecimal(CS_kwh_currentdata!![i].getTotalConsumptionAmount()))
                                System.out.println("current value for " + CS_kwh_currentdata!![i].getMonth() + "," + CS_kwh_currentdata!![i].getYear() + ": " + CS_kwh_currentdata!![i].getTotalConsumptionAmount())
                            }
                        }
                    }

                    for (i in CS_kwh_previousdata!!.indices) {
                        if (!IsDollar) {
                            if (!CS_kwh_previousdata!![i].getTotalConsumption().equalsIgnoreCase("")) {
                                previousvalues.add(java.lang.Float.parseFloat(isConvertTODecimal(CS_kwh_previousdata!![i].getTotalConsumption())))
                                previousarray[i] =
                                    java.lang.Double.parseDouble(isConvertTODecimal(CS_kwh_previousdata!![i].getTotalConsumption()))
                                System.out.println("Previous value for " + CS_kwh_previousdata!![i].getMonth() + "," + CS_kwh_previousdata!![i].getYear() + ": " + CS_kwh_previousdata!![i].getTotalConsumption())
                            }
                        } else if (IsDollar) {
                            if (!CS_kwh_previousdata!![i].getTotalConsumptionAmount().equalsIgnoreCase("")) {
                                previousvalues.add(java.lang.Float.parseFloat(isConvertTODecimal(CS_kwh_previousdata!![i].getTotalConsumptionAmount())))
                                previousarray[i] =
                                    java.lang.Double.parseDouble(isConvertTODecimal(CS_kwh_previousdata!![i].getTotalConsumptionAmount()))
                                System.out.println("Previous value for " + CS_kwh_previousdata!![i].getMonth() + "," + CS_kwh_previousdata!![i].getYear() + ": " + CS_kwh_previousdata!![i].getTotalConsumptionAmount())
                            }
                        }
                    }

                    values.add(currentarray)
                    values.add(previousarray)

                    val maxvaluecurrentchart = Collections.max(currentvalues)
                    val maxvaluepreviouschart = Collections.max(previousvalues)

                    val minvaluecurrentchart = Collections.min(currentvalues)
                    val minvaluepreviouschart = Collections.min(previousvalues)

                    val wholeminvalue = Math.min(minvaluecurrentchart, minvaluepreviouschart)
                    val wholemaxvalue = Math.max(maxvaluecurrentchart, maxvaluepreviouschart)

                    //-------MP chart work-----------------------------------------------

                    //compareSpendingLineChart.setDrawValueAboveBar(true);
                    compareSpendingLineChart!!.setDescription("")
                    //compareSpendingLineChart.setHighlightEnabled(false);
                    compareSpendingLineChart!!.setDrawGridBackground(false)
                    //compareSpendingLineChart.setDrawBarShadow(false);
                    compareSpendingLineChart!!.isDoubleTapToZoomEnabled = false
                    compareSpendingLineChart!!.setPinchZoom(false)
                    compareSpendingLineChart!!.setScaleEnabled(false)
                    //compareSpendingLineChart.getAxisName().setPosition(AxisName.AxisNamePosition.BELOW_CHART_CENTER);
                    //compareSpendingLineChart.getAxisName().setTextColor(Color.parseColor("#000000"));

                    val l = compareSpendingLineChart!!.legend
                    l.isEnabled = false
                    val rightAxis = compareSpendingLineChart!!.axisRight
                    rightAxis.isEnabled = false

                    val leftAxis = compareSpendingLineChart!!.axisLeft
                    leftAxis.setDrawGridLines(true)
                    leftAxis.valueFormatter = MyValueFormatter()
                    leftAxis.setAxisMaxValue((wholemaxvalue * 1.1).toFloat())
                    leftAxis.setAxisMinValue(wholeminvalue)

                    val xAxis = compareSpendingLineChart!!.xAxis
                    //xAxis.setmPositionRotation(XAxisRotation.ANGLE_315.getValue());
                    xAxis.setDrawGridLines(false)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setLabelsToSkip(0)
                    xAxis.labelRotationAngle = 315f
                    xAxis.textSize = 7f

                    val xVals = ArrayList<String>()
                    monthslist.clear()
                    for (i in CS_kwh_currentdata!!.indices) {
                        val dayofmonth =
                            setMonth(CS_kwh_currentdata!![i].getMonth()) + " " + CS_kwh_currentdata!![i].getYear()

                        monthslist.add(setMonth(CS_kwh_currentdata!![i].getMonth()) + ", " + CS_kwh_currentdata!![i].getYear())
                        xVals.add(i, "" + dayofmonth)
                    }

                    var ylabel = ""
                    tv_selectedmonth.text =
                        setMonth(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getMonth()) + ", " + CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getYear()
                    selectedmonth = CS_kwh_currentdata!!.size - 1
                    if (IsDollar) {
                        ylabel = DBNew!!.getLabelText("ML_Graph_Lbl_Nrml_Dollar", languageCode)
                        tv_yaxistitle.setText(DBNew!!.getLabelText("ML_Graph_Lbl_Nrml_Dollar", languageCode))

                        tv_usagesofarvalue!!.text =
                            "$" + isConvertTODecimal(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumptionAmount())
                    } else {
                        if (tabselected!!.equals("gas", ignoreCase = true)) {
                            ylabel = "Units consumed (CCF)"
                            tv_yaxistitle.setText("Units consumed (CCF)")
                            tv_usagesofarvalue!!.text =
                                isConvertTODecimal(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumption()) + " CCF"
                        } else {
                            ylabel = DBNew!!.getLabelText("ML_Graph_Lbl_Nrml_Kwh", languageCode)
                            tv_yaxistitle.setText(DBNew!!.getLabelText("ML_Graph_Lbl_Nrml_Kwh", languageCode))
                            tv_usagesofarvalue!!.text =
                                isConvertTODecimal(CS_kwh_currentdata!![CS_kwh_currentdata!!.size - 1].getTotalConsumptionAmount()) + " kWh"
                        }
                    }

                    //LineData d = compareMeLineChartData(currentvalues,previousvalues,xVals,ylabel);
                    val data = CombinedData(xVals)
                    data.setData(compareMeLineChartData(currentvalues, previousvalues, xVals, ylabel))

                    compareSpendingLineChart!!.data = data
                    compareSpendingLineChart!!.invalidate()
                    //compareSpendingLineChart.setVisibleXRange(getResources().getInteger(R.dimen.comparespendingbarnumber));
                    // compareSpendingLineChart.setVisibleXRange(5);

                    compareSpendingLineChart!!.animateY(2500)
                    compareSpendingLineChart!!.animateX(2500, Easing.EasingOption.EaseInOutQuart)
                    compareSpendingLineChart!!.setDrawBarShadow(false)
                    compareSpendingLineChart!!.setVisibleXRange(2f, 12f)
                    compareSpendingLineChart!!.invalidate()
                    //-------MP chart work-----------------------finish------------------------

                    // showing comparision legends
                    compareSpendingLineChart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {

                        override fun onNothingSelected() {}

                        fun onValueSelected(
                            e: Entry, dataSetIndex: Int,
                            h: Highlight
                        ) {

                            compareSpendingLineChart!!.setDrawBarShadow(false)
                            compareSpendingLineChart!!.invalidate()
                            try {
                                var currentmonthyear = ""
                                var currentrangevalue = ""
                                var previousmonthyear = ""
                                var previousrangevalue = ""

                                if (!IsDollar) {
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![e.getXIndex()].getMonth()) + ", " + CS_kwh_currentdata!![e.getXIndex()].getYear()
                                    currentrangevalue = CS_kwh_currentdata!![e.getXIndex()].getTotalConsumption()

                                    previousmonthyear =
                                        setMonth(CS_kwh_previousdata!![e.getXIndex()].getMonth()) + ", " + CS_kwh_previousdata!![e.getXIndex()].getYear()
                                    previousrangevalue = CS_kwh_previousdata!![e.getXIndex()].getTotalConsumption()
                                } else {
                                    currentmonthyear =
                                        setMonth(CS_kwh_currentdata!![e.getXIndex()].getMonth()) + ", " + CS_kwh_currentdata!![e.getXIndex()].getYear()
                                    currentrangevalue = CS_kwh_currentdata!![e.getXIndex()].getTotalConsumptionAmount()

                                    previousmonthyear =
                                        setMonth(CS_kwh_previousdata!![e.getXIndex()].getMonth()) + ", " + CS_kwh_previousdata!![e.getXIndex()].getYear()
                                    previousrangevalue =
                                        CS_kwh_previousdata!![e.getXIndex()].getTotalConsumptionAmount()
                                }

                                showcomparelegends(
                                    currentmonthyear,
                                    currentrangevalue,
                                    previousmonthyear,
                                    previousrangevalue,
                                    "",
                                    "",
                                    "",
                                    ""
                                )
                            } catch (e1: Exception) {
                                e1.printStackTrace()
                            }

                        }
                    })

                    var currentmonthyear = ""
                    var currentrangevalue = ""
                    var previousmonthyear = ""
                    var previousrangevalue = ""

                    if (!IsDollar) {
                        currentmonthyear =
                            setMonth(CS_kwh_currentdata!![result!! - 1].getMonth()) + ", " + CS_kwh_currentdata!![result - 1].getYear()
                        currentrangevalue = isConvertTODecimal(CS_kwh_currentdata!![result - 1].getTotalConsumption())

                        previousmonthyear =
                            setMonth(CS_kwh_previousdata!![result - 1].getMonth()) + ", " + CS_kwh_previousdata!![result - 1].getYear()
                        previousrangevalue = isConvertTODecimal(CS_kwh_previousdata!![result - 1].getTotalConsumption())
                    } else {
                        currentmonthyear =
                            setMonth(CS_kwh_currentdata!![result!! - 1].getMonth()) + ", " + CS_kwh_currentdata!![result - 1].getYear()
                        currentrangevalue =
                            isConvertTODecimal(CS_kwh_currentdata!![result - 1].getTotalConsumptionAmount())

                        previousmonthyear =
                            setMonth(CS_kwh_previousdata!![result - 1].getMonth()) + ", " + CS_kwh_previousdata!![result - 1].getYear()
                        previousrangevalue =
                            isConvertTODecimal(CS_kwh_previousdata!![result - 1].getTotalConsumptionAmount())
                    }
                    showcomparelegends(
                        currentmonthyear,
                        currentrangevalue,
                        previousmonthyear,
                        previousrangevalue,
                        "",
                        "",
                        "",
                        ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    internal fun compareMeLineChartData(
        currentvalues: List<Float>,
        previousvalues: List<Float>,
        xVals: ArrayList<String>,
        ylabel: String
    ): LineData? {

        var f: Float?
        var d: LineData? = null
        //----first line------------------------------------------------------
        val dataSets = ArrayList<ILineDataSet>()
        try {
            val floatArray = FloatArray(currentvalues.size)
            for (i in currentvalues.indices) {
                f = currentvalues[i]
                floatArray[i] = f ?: java.lang.Float.NaN
            }

            val yVals1 = ArrayList<Entry>()
            for (i in floatArray.indices) {
                if (floatArray[i] >= 0) {
                    val usagevalue = floatArray[i]

                    if (usagevalue >= 0.0)
                        yVals1.add(Entry(usagevalue, i.toFloat()))
                } else
                    yVals1.add(Entry(0f, i.toFloat()))
            }

            val set1 = LineDataSet(yVals1, "")
            set1.setDrawValues(false)
            set1.valueTextSize = 7f
            set1.color = Color.parseColor(currentColor)
            set1.valueTextColor = Color.parseColor(currentColor)
            set1.fillColor = Color.parseColor(currentColor)
            set1.setCircleColor(Color.parseColor(currentColor))
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.lineWidth = 2.5f
            set1.circleSize = 5f
            if (IsDollar)
                set1.valueFormatter = MyValueFormatter()
            else
                set1.valueFormatter = MyValueFormatter()

            ///---second line------------------------------------------------------------
            val floatArray2 = FloatArray(previousvalues.size)
            for (i in previousvalues.indices) {
                f = previousvalues[i]
                floatArray2[i] = f ?: java.lang.Float.NaN
            }

            val yVals2 = ArrayList<Entry>()
            for (i in floatArray2.indices) {
                if (floatArray2[i] >= 0) {
                    val usagevalue2 = floatArray2[i]

                    if (usagevalue2 >= 0.0)
                        yVals2.add(Entry(usagevalue2, i.toFloat()))
                } else
                    yVals2.add(Entry(0f, i.toFloat()))
            }

            //		 compareSpendingLineChart.getAxisName().setPosition(AxisName.AxisNamePosition.BELOW_CHART_CENTER);
            //			compareSpendingLineChart.getAxisName().setTextColor(Color.parseColor("#000000"));
            // create a dataset and give it a type

            val set2 = LineDataSet(yVals2, "1234567")
            set2.setDrawValues(false)
            set2.valueTextSize = 7f
            set2.color = Color.parseColor(previousColor)
            set2.valueTextColor = Color.parseColor(previousColor)
            set2.fillColor = Color.parseColor(previousColor)
            set2.setCircleColor(Color.parseColor(previousColor))
            set2.highLightColor = Color.rgb(244, 117, 117)
            set2.lineWidth = 2.5f
            set2.circleSize = 5f

            if (IsDollar)
                set2.valueFormatter = MyValueFormatter()
            else
                set2.valueFormatter = MyValueFormatter()

            set1.axisDependency = YAxis.AxisDependency.LEFT
            set2.axisDependency = YAxis.AxisDependency.LEFT

            dataSets.add(set1)
            dataSets.add(set2)
            d = LineData(xVals, dataSets)

            //d.setGroupSpace(80f);
            d.setValueTextSize(7f)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        return d
    }

    companion object {

        var CS_dollar_currentdata: ArrayList<NewCompareSpending_Monthly_Dataset>? = null
        var CS_dollar_previousdata: ArrayList<NewCompareSpending_Monthly_Dataset>? = null
        var CS_kwh_currentdata: ArrayList<NewCompareSpending_Monthly_Dataset>? = null
        var CS_kwh_previousdata: ArrayList<NewCompareSpending_Monthly_Dataset>? = null

        var usagerangedata = ArrayList<UsagesRangeDataset>()

        var expectedusagesdata: ArrayList<UsagesDashboardDetailDataset>? = ArrayList<UsagesDashboardDetailDataset>()
        var gasexpectedusagesdata: ArrayList<UsagesDashboardDetailDataset>? = ArrayList<UsagesDashboardDetailDataset>()
    }

}*/
