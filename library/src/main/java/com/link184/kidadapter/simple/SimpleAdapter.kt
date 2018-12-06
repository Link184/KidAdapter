package com.link184.kidadapter.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.base.BaseAdapter
import com.link184.kidadapter.base.BaseViewHolder

open class SimpleAdapter<T> (private val configuration: SimpleAdapterConfiguration<T>)
    : BaseAdapter<T, BaseViewHolder<T>>(configuration.items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(configuration.layoutResId, parent, false)
        val viewHolder = object : BaseViewHolder<T>(view) {
            override fun bindView(item: T) {
                configuration.bindHolder(itemView, item)
            }
        }
        val itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        return viewHolder
    }

    open fun onItemClick(itemView: View, position: Int) {}
}