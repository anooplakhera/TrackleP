package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses

object UserMeterListData {
    var mArrayList: ArrayList<ResponseModelClasses.MeterDetails.Results1.TableOne>? = null

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.MeterDetails.Results1.TableOne>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = mArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.MeterDetails.Results1.TableOne>) {
        mArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.MeterDetails.Results1.TableOne>? {
        return mArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.MeterDetails.Results1.TableOne {
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


    @Synchronized
    fun getMeterNumberList(): ArrayList<String> {
        var array = ArrayList<String>()
        try {
            for (i in 0 until getCount()) {
                array.add(mArrayList?.get(i)!!.MeterNumber)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array

    }

}


