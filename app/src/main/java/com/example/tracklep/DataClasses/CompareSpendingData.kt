package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.Utils.Utils

object CompareSpendingData {
    var mArrayList1: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table1C>? = null
    var mArrayList2: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table2C>? = null
    var mArrayList3: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table3C>? = null
    var mArrayList4: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table4C>? = null
    var mArrayList5: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table5C>? = null


    init {
        if (mArrayList1 == null)
            mArrayList1 = ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table1C>()
        if (mArrayList2 == null)
            mArrayList2 = ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table2C>()
        if (mArrayList3 == null)
            mArrayList3 = ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table3C>()
        if (mArrayList4 == null)
            mArrayList4 = ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table4C>()
        if (mArrayList5 == null)
            mArrayList5 = ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table5C>()

    }

    @Synchronized
    fun getCount1(): Int {
        var count = 0
        count = mArrayList1!!.size
        return count
    }

    @Synchronized
    fun getCount2(): Int {
        var count = 0
        count = mArrayList2!!.size
        return count
    }

    @Synchronized
    fun getCount3(): Int {
        var count = 0
        count = mArrayList3!!.size
        return count
    }

    @Synchronized
    fun getCount4(): Int {
        var count = 0
        count = mArrayList4!!.size
        return count
    }

    @Synchronized
    fun getCount5(): Int {
        var count = 0
        count = mArrayList5!!.size
        return count
    }

    @Synchronized
    fun addArrayList1(modelList: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table1C>) {
        mArrayList1 = modelList
    }

    @Synchronized
    fun addArrayList2(modelList: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table2C>) {
        mArrayList2 = modelList
    }

    @Synchronized
    fun addArrayList3(modelList: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table3C>) {
        mArrayList3 = modelList
    }

    @Synchronized
    fun addArrayList4(modelList: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table4C>) {
        mArrayList4 = modelList
    }

    @Synchronized
    fun addArrayList5(modelList: ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table5C>) {
        mArrayList5 = modelList
    }

    @Synchronized
    fun getArrayList1(): ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table1C>? {
        return mArrayList1
    }

    @Synchronized
    fun getArrayList2(): ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table2C>? {
        return mArrayList2
    }

    @Synchronized
    fun getArrayList3(): ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table3C>? {
        return mArrayList3
    }

    @Synchronized
    fun getArrayList4(): ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table4C>? {
        return mArrayList4
    }

    @Synchronized
    fun getArrayList5(): ArrayList<ResponseModelClasses.CompareDataResponse.Results1C.Table5C>? {
        return mArrayList5
    }


    @Synchronized
    fun clearArrayList() {
        mArrayList1!!.removeAll(mArrayList1!!)
        mArrayList2!!.removeAll(mArrayList2!!)
        mArrayList3!!.removeAll(mArrayList3!!)
        mArrayList4!!.removeAll(mArrayList4!!)
        mArrayList5!!.removeAll(mArrayList5!!)
    }

    @Synchronized
    fun getCurrentBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        for (i in 0 until getCount1()) {
            array.add(
                ResponseModelClasses.BarChart(
                    mArrayList1!!.get(i).Consumed.toFloat(),
                    mArrayList1!!.get(i).MOD.toFloat()
                )
            )
        }
        return array
    }

    @Synchronized
    fun getPreviousBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        for (i in 0 until getCount2()) {
            array.add(
                ResponseModelClasses.BarChart(
                    mArrayList2!!.get(i).Consumed.toFloat(),
                    mArrayList2!!.get(i).MOD.toFloat()
                )
            )
        }
        return array
    }

    @Synchronized
    fun getUtilityBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        for (i in 0 until getCount3()) {
            array.add(
                ResponseModelClasses.BarChart(
                    mArrayList3!!.get(i).Consumed.toFloat(),
                    mArrayList3!!.get(i).MOD.toFloat()
                )
            )
        }
        return array
    }

    @Synchronized
    fun getZipBar(): ArrayList<ResponseModelClasses.BarChart> {
        val array = ArrayList<ResponseModelClasses.BarChart>()
        for (i in 0 until getCount4()) {
            array.add(
                ResponseModelClasses.BarChart(
                    mArrayList4!!.get(i).Consumed.toFloat(),
                    mArrayList4!!.get(i).MOD.toFloat()
                )
            )
        }
        return array
    }

    @Synchronized
    fun getYearBar(): ArrayList<String> {
        val array = ArrayList<String>()
        for (i in 0 until getCount2()) {
            array.add(
                Utils.getNameOfMonth(mArrayList2!!.get(i).MOD) + ", " + mArrayList2!!.get(i).YOD)
        }
        return array
    }


    @Synchronized
    fun getCompareMeTitle(): String {
        var title = ""
        title += Utils.getNameOfMonth(mArrayList1!![0].MOD) + " " + mArrayList1!![0].YOD + " to " + Utils.getNameOfMonth(
            mArrayList2!![getCount2() - 1].MOD
        ) + " " + mArrayList2!![getCount2() - 1].YOD
        return title
    }

    @Synchronized
    fun getUtilityTitle(): String {
        var title = ""
        title += Utils.getNameOfMonth(mArrayList1!![0].MOD) + " " + mArrayList1!![0].YOD + " to " + Utils.getNameOfMonth(
            mArrayList3!![getCount2() - 1].MOD
        ) + " " + mArrayList3!![getCount2() - 1].YOD
        return title
    }

    @Synchronized
    fun getZipTitle(): String {
        var title = ""
        title += Utils.getNameOfMonth(mArrayList1!![0].MOD) + " " + mArrayList1!![0].YOD + " to " + Utils.getNameOfMonth(
            mArrayList4!![getCount2() - 1].MOD
        ) + " " + mArrayList4!![getCount2() - 1].YOD
        return title
    }


}


