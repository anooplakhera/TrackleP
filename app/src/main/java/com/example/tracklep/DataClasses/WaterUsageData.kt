package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses

object WaterUsageData {
    var mArrayList: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>? = null

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = mArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.WaterUsages.Results1.TableOne>) {
        mArrayList = modelList
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
//                array.add(Utils.parseDateToddMMyyyy(mArrayList?.get(i)!!.UsageDate))
                array.add(mArrayList?.get(i)!!.UsageDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array
    }

    @Synchronized
    fun getUsageBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        try {
            for (i in 0 until getCount()) {
                array.add(
                    ResponseModelClasses.BarChart(
                        mArrayList!!.get(i).AllocationValue.toFloat(),
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


}


