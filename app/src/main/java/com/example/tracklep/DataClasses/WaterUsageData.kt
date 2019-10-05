package com.example.tracklep.DataClasses

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.Utils.AppLog
import com.example.tracklep.Utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

object WaterUsageData {
    var mArrayList: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>? = null
    var mArrayListHourly: ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne>? =
        null

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>()
        if (mArrayListHourly == null)
            mArrayListHourly = ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        if (mArrayList!!.size > 0)
            count = mArrayList!!.size
        else if (mArrayListHourly!!.size > 0)
            count = mArrayListHourly!!.size
        return count
    }

    @Synchronized
    fun getCountHourly(): Int {
        var count = 0
        count = mArrayListHourly!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>) {
        mArrayList = modelList
    }

    @Synchronized
    fun addArrayListHourly(modelList: ArrayList<ResponseModelClasses.WaterUsagesHourly.Results1.TableOne>) {
        mArrayListHourly = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>? {
        return mArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.WaterUsages.Results1.TableOne {
        return mArrayList!!.get(position)
    }

    @Synchronized
    fun clearListItem(index: Int) {
        mArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearArrayList() {
        mArrayList!!.removeAll(mArrayList!!)

    }

    //Graph - Work
    @Synchronized
    fun getMonthList(): ArrayList<String> {
        var array = ArrayList<String>()
        try {
            for (i in 0 until getCount()) {
                array.add(Utils.parseDateToddMMyyyy(mArrayList?.get(i)!!.UsageDate))
//                array.add(mArrayList?.get(i)!!.UsageDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getMonthListMonthly(): ArrayList<String> {
        var array = ArrayList<String>()
        try {
            for (i in 0 until getCount()) {
                var month =
                    Utils.getNameOfMonth(mArrayList!!.get(i).Month) + ", " + mArrayList!!.get(i).Year
                array.add(month)
//                array.add(mArrayList?.get(i)!!.UsageDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getMonthListHourly(): ArrayList<String> {
        var array = ArrayList<String>()
        try {
            for (i in 0 until getCountHourly()) {
//                array.add(Utils.parseDateToddMMyyyy(mArrayListHourly?.get(i)!!.UsageDate))
                array.add(mArrayListHourly?.get(i)!!.Hourly)
//                array.add(mArrayList?.get(i)!!.UsageDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized
    fun getUsagePeriod(mMode: String): String {
        var title = ""
        /*if (mArrayList!!.size > 0) {
            if (mArrayList!![0].Month != null && mArrayList!![0].Year != null) {
                title += Utils.getNameOfMonth(mArrayList!![0].Month) + " " + mArrayList!![0].Year + " To " + Utils.getNameOfMonth(
                    mArrayList!![getCount() - 1].Month
                ) + " " + mArrayList!![getCount() - 1].Year
            } else {
                title += mArrayList!![0].UsageDate
            }
        } else if (mArrayListHourly!!.size > 0) {
            if (mArrayListHourly!![0].Hourly != null) {
                title += mArrayListHourly!![0].Hourly + " To " + mArrayListHourly!![getCount() - 1].Hourly

            } else {
                title += mArrayListHourly!![0].UsageDate
            }
        }*/

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
        val formatter2 = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH)

        if (mMode == "H") {
            val date = LocalDate.parse(mArrayListHourly!![0].UsageDate, formatter)

            val format_date = date.format(formatter2)

            title = "Hourly usage for " + format_date
        } else if (mMode == "D" || mMode == "M" || mMode == "B") {

            val dateFrom = LocalDate.parse(mArrayList!![0].UsageDate, formatter)
            val dateTo = LocalDate.parse(mArrayList!![getCount() - 1].UsageDate, formatter)

            val format_dateFrom = dateFrom.format(formatter2)
            val format_dateTo = dateTo.format(formatter2)

            title = format_dateFrom + " To " + format_dateTo
        }

        return title
    }

    @Synchronized
    fun getUsageBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCount()) {
                if (mArrayList!![i].AllocationValue != null) {
                    array.add(
                        ResponseModelClasses.BarChart(
                            mArrayList!![i].AllocationValue.toFloat(),
                            mArrayList!![i].HIGHEST.toFloat()
                        )
                    )
                } else {
                    array.add(
                        ResponseModelClasses.BarChart(
                            0f,
                            mArrayList!![i].HIGHEST.toFloat()
                        )
                    )
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getUsageBarHourly(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCountHourly()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        0f,
                        mArrayListHourly!![i].HIGHEST.toFloat()
                    )
                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getUsageConsumedBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCount()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        mArrayList!!.get(i).TotalValue.toFloat(),
                        mArrayList!!.get(i).HIGHEST.toFloat()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }


    @Synchronized
    fun getUsageConsumedBarHourly(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCountHourly()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        mArrayListHourly!!.get(i).TotalValue.toFloat(),
                        mArrayListHourly!!.get(i).HIGHEST.toFloat()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }


    @Synchronized
    fun getUsageEmptyBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCount()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        0.0f,
                        0.0f
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getUsageEmptyBarHourly(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCountHourly()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        0.0f,
                        0.0f
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }


}


