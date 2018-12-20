package com.link184.kidadapter.base

import android.support.v7.util.DiffUtil

internal class KidDiffUtilCallback<T>(
    private val oldItems: MutableList<T>,
    private val newItems: MutableList<T>,
    internal var contentComparator: ((T, T) -> Boolean)? = null,
    internal var itemsComparator: ((T, T) -> Boolean)? = null
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return itemsComparator?.invoke(oldItems[oldItemPosition], newItems[newItemPosition])
                ?: oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return contentComparator?.invoke(oldItems[oldItemPosition], newItems[newItemPosition])
            ?: (oldItems[oldItemPosition] == newItems[newItemPosition])
    }
}