package com.example.tracklep.DataClasses

import com.example.tracklep.DataModels.ResponseModelClasses
import java.util.*
import kotlin.collections.ArrayList

object ProfileListData {
    var mArrayList: ArrayList<ResponseModelClasses.MyProfile>? = null

    var hashMapSelected = HashMap<Int, Boolean>()

    init {
        if (mArrayList == null)
            mArrayList = ArrayList<ResponseModelClasses.MyProfile>()
    }

    @Synchronized
    fun getCount(): Int {
        var count = 0
        count = mArrayList!!.size
        return count
    }

    @Synchronized
    fun addArrayList(modelList: ArrayList<ResponseModelClasses.MyProfile>) {
        mArrayList = modelList
    }

    @Synchronized
    fun getArrayList(): ArrayList<ResponseModelClasses.MyProfile>? {
        return mArrayList
    }

    @Synchronized
    fun getArrayItem(position: Int): ResponseModelClasses.MyProfile {
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
    fun saveItemInHashMap() {
        for (i in 0 until getCount()) {
            hashMapSelected[i] = false
        }
    }

    @Synchronized
    fun getSelectedItemIndex(position: Int) {
        hashMapSelected[position] = true
        for (i in 0 until hashMapSelected.size) {
            if (i != position)
                hashMapSelected[i] = false
        }
    }

    @Synchronized
    fun getArrayAddress(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until getCount()) {
            list.add(getArrayItem(i).CommunicationAddress)
        }
        return list
    }

    @Synchronized
    fun getPositionDefault(): Int {
        var position = 0
        for (i in 0 until getCount()) {
            if (getArrayItem(i).DefaultAddressId != 0) {
                position = i
                break
            }
        }
        return position
    }

}


