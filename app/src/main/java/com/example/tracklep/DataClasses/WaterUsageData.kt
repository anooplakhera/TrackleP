package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.Utils.Utils

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
                array.add(Utils.getNameOfMonth(mArrayList?.get(i)!!.Month + ", " + mArrayList?.get(i)!!.Year))
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
                array.add(Utils.parseDateToddMMyyyy(mArrayListHourly?.get(i)!!.UsageDate))
//                array.add(mArrayList?.get(i)!!.UsageDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getUsagePeriod(): String {
        var title = ""
        title += Utils.getNameOfMonth(mArrayList!![getCount() - 1].Month) + " " + mArrayList!![getCount() - 1].Year + " to " + Utils.getNameOfMonth(
            mArrayList!![0].Month
        ) + " " + mArrayList!![0].Year

        return title
    }

    @Synchronized
    fun getUsageBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCount()) {
                if (mArrayList!![i].AllocationValue.toFloat() != null) {
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


