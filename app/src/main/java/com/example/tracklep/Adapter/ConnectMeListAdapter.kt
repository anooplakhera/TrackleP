package com.example.tracklep.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tracklep.DataClasses.ConnectMeData
import com.example.tracklep.R
import com.example.tracklep.Utils.AppConstants
import kotlinx.android.synthetic.main.question_list_layout.view.*

class ConnectMeListAdapter(val itemClick: (Int) -> Unit) : RecyclerView.Adapter<ConnectMeListAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.question_list_layout, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return ConnectMeData.getCount()
    }

    override fun onBindViewHolder(holder: MyHolder, p1: Int) {
        holder.bindData(p1)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(int: Int) {
            val data = ConnectMeData.getArrayItem(int)
            itemView.txtQuestion.text = data.TopicName
            AppConstants.SelectedTopicID = data.TopicID.toString()
            itemView.txtQuestion.setOnClickListener {
                itemClick(adapterPosition)
            }
        }
    }
}