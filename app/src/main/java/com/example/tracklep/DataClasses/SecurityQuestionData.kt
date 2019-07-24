package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses
import java.util.*
import kotlin.collections.ArrayList

object SecurityQuestionData {
    var questArrayList: ArrayList<ResponseModelClasses.SecurityQuestionResponse>? = null

    init {
        if (questArrayList == null)
            questArrayList = ArrayList<ResponseModelClasses.SecurityQuestionResponse>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = questArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.SecurityQuestionResponse>) {
        questArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.SecurityQuestionResponse>? {
        return questArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.SecurityQuestionResponse {
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

    @Synchronized
    fun getQuestionName(id: String): String {
        var ques = ""
        for (i in 0 until getCount()) {
            if (questArrayList!!.get(i).SecurityQuestionId.equals(id)) {
                ques = questArrayList!![i].Question
            }
        }
        return ques
    }

    @Synchronized
    fun getArrayQuestion(): Array<String> {
        val list: ArrayList<String> = arrayListOf()
        for (i in 0 until getCount()) {
            list.add(getArrayItem(i).Question)
        }
        val array = arrayOfNulls<String>(list.size)
        list.toArray(array)
        return arrayOf(Arrays.toString(array))
    }

}


