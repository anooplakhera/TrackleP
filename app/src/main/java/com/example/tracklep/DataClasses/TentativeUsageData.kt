package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses

object TentativeUsageData {
    var mArrayList: ArrayList<ResponseModelClasses.TentativeDetails.Results1.TableTwo>? = null

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.TentativeDetails.Results1.TableTwo>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = mArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.TentativeDetails.Results1.TableTwo>) {
        mArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.TentativeDetails.Results1.TableTwo>? {
        return mArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.TentativeDetails.Results1.TableTwo {
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
    fun getTentativeUsageDetailsList(): ArrayList<String> {
        var array = ArrayList<String>()
        try {
            for (i in 0 until getCount()) {
                array.add(mArrayList?.get(i)!!.AccountNumber)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return array

    }

}
