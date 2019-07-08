package com.example.tracklep.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tracklep.DataClasses.SecurityQuestionData
import com.example.tracklep.R
import kotlinx.android.synthetic.main.question_list_layout.view.*

class QuestionListAdapter(val itemClick: (Int) -> Unit) : RecyclerView.Adapter<QuestionListAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.question_list_layout, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return SecurityQuestionData.getCount()
    }

    override fun onBindViewHolder(holder: MyHolder, p1: Int) {
        holder.bindData(p1)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(int: Int) {
            val data = SecurityQuestionData.getArrayItem(int)
            itemView.txtQuestion.text = data.Question
            itemView.txtQuestion.setOnClickListener {
                itemClick(adapterPosition)
            }
        }
    }
}