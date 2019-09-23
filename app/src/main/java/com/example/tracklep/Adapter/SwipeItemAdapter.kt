package com.example.tracklep.Adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tracklep.R
import kotlinx.android.synthetic.main.row_item.view.*

class SwipeItemAdapter(private val items: ArrayList<String>, private val itemClick: (Int) -> Unit) :
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
        if (items != null && items.size < 0) {
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
