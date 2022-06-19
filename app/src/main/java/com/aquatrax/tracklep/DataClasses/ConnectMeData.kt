package com.aquatrax.tracklep.DataClasses

import com.aquatrax.tracklep.DataModels.ResponseModelClasses

object ConnectMeData {
    var mArrayList: ArrayList<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo>? = null

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = mArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo>) {
        mArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo>? {
        return mArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.ConnectWithUtilityResponse.Results1.TableTwo {
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

}


