package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses

object UtilitiesData {
    var utilityArrayList: ArrayList<ResponseModelClasses.UtilityListResponseModel.Results1.Table1>? = null

    init {
        if (utilityArrayList == null)
            utilityArrayList = ArrayList<ResponseModelClasses.UtilityListResponseModel.Results1.Table1>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = utilityArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.UtilityListResponseModel.Results1.Table1>) {
        utilityArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.UtilityListResponseModel.Results1.Table1>? {
        return utilityArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.UtilityListResponseModel.Results1.Table1 {
        return utilityArrayList!!.get(position)
    }

    @Synchronized
    fun clearListItem(index: Int) {
        utilityArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearArrayList() {
        utilityArrayList!!.removeAll(utilityArrayList!!)

    }


}


