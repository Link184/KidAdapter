package com.link184.kidadapter.typed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.base.BaseAdapter
import com.link184.kidadapter.base.BaseViewHolder

open class MultiTypeAdapter(
        private val multiAdapterDsl: MultiAdapterDsl
) : BaseAdapter<Any, BaseViewHolder<Any>>(multiAdapterDsl.getAllItems()) {

    override fun getItemViewType(position: Int): Int {
        return multiAdapterDsl.viewTypes.first { it.positionRange.contains(position) }.viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val adapterViewType = multiAdapterDsl.viewTypes.first { it.viewType == viewType }
        val view = LayoutInflater.from(parent.context).inflate(adapterViewType.configuration.layoutResId, parent, false)
        val viewHolder = object : BaseViewHolder<Any>(view) {
            override fun bindView(item: Any) {
                adapterViewType.configuration.bindHolder(itemView, item)
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

    fun update(block: UpdateConfiguration.() -> Unit) {
        UpdateConfiguration().apply(block).doUpdate(multiAdapterDsl)
        this += multiAdapterDsl.getAllItems()
    }
}
