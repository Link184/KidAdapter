package com.link184.kidadapter.typed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.base.BaseAdapter
import com.link184.kidadapter.base.BaseViewHolder

open class MultiTypeAdapter(
    private val multiAdapterConfiguration: MultiAdapterConfiguration
) : BaseAdapter<Any, BaseViewHolder<Any>>(multiAdapterConfiguration.getAllItems()) {

    override fun getItemViewType(position: Int): Int {
        return multiAdapterConfiguration.viewTypes.first { it.positionRange.contains(position) }.viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val adapterViewType = multiAdapterConfiguration.viewTypes.first { it.viewType == viewType }
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
        val diffCallbacks = UpdateConfiguration().apply(block).doUpdate(multiAdapterConfiguration)
        this += multiAdapterConfiguration.getAllItems()
        // todo: hack to avoid : java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 1(offset:0).state:17
        if (itemList.size > 0) {
            diffCallbacks
                .filterNotNull()
                .forEach {
                    DiffUtil.calculateDiff(it).dispatchUpdatesTo(this)
                }
        } else {
            notifyDataSetChanged()
        }
        itemList.recycle()
    }

    fun <T> getItemsByType(tag: String? = null): MutableList<T> {
        if (tag != null) {
            return multiAdapterConfiguration.getViewTypeByTag(tag).configuration.getInternalItems() as MutableList<T>
        }
        return multiAdapterConfiguration.getItemsByType<Any>() as MutableList<T>
    }
}
