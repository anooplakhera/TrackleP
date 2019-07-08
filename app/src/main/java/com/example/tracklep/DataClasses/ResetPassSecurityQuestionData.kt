package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses

object ResetPassSecurityQuestionData {
    var questArrayList: ArrayList<ResponseModelClasses.ResetPassStep1Response.TableData>? = null

    init {
        if (questArrayList == null)
            questArrayList = ArrayList<ResponseModelClasses.ResetPassStep1Response.TableData>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = questArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.ResetPassStep1Response.TableData>) {
        questArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.ResetPassStep1Response.TableData>? {
        return questArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.ResetPassStep1Response.TableData {
        return questArrayList!!.get(position)
    }

    @Synchronized
    fun clearListItem(index: Int) {
        questArrayList!!.removeAt(index)
    }

    @Synchronized
    fun clearArrayList() {
        questArrayList!!.removeAll(questArrayList!!)

    }


}


