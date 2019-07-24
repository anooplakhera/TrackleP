package com.example.tracklep.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tracklep.R
import kotlinx.android.synthetic.main.row_item.view.*

class SwipeItemAdapter(private val items: MutableList<String>, private val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<SwipeItemAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun isEmpty(): Boolean {
        var isEmpty = false
        if (items.isNullOrEmpty()) {
            isEmpty = true
        }
        return isEmpty
    }

    inner class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
    ) {

        fun bind(name: String) = with(itemView) {
            rowName.text = name
            itemView.setOnClickListener {
                itemClick(adapterPosition)
            }
        }
    }
}
