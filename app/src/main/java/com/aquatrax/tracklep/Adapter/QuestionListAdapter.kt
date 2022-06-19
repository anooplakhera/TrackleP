package com.aquatrax.tracklep.Adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aquatrax.tracklep.DataClasses.SecurityQuestionData
import com.aquatrax.tracklep.R
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
            if (SecurityQuestionData.hashMapSelected[int]!!) {
                itemView.visibility = View.GONE
                itemView.layoutParams = RecyclerView.LayoutParams(0, 0);
            } else {
                itemView.visibility = View.VISIBLE
                itemView.layoutParams =
                    RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            itemView.txtQuestion.text = data.Question
            itemView.txtQuestion.setOnClickListener {
                itemClick(adapterPosition)
                SecurityQuestionData.getSelectedItemIndex(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}