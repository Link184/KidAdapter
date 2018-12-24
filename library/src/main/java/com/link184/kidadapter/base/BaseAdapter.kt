package com.link184.kidadapter.base

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T, H : BaseViewHolder<T>>(protected var itemList: KidList<T>) : RecyclerView.Adapter<H>() {
    constructor(itemList: MutableList<T>) : this(KidList(itemList))

    override fun getItemCount() = itemList.size

    final override fun onBindViewHolder(holder: H, position: Int) {
        holder.bindView(itemList[position])
    }

    /**
     * Reset all items from adapter
     * @param itemList new adapter items
     */
    open operator fun plusAssign(itemList: MutableList<T>) {
        this.itemList.reset(itemList)
    }

    /**
     * Add new items to already existing adapter items
     * @param itemList new adapter items
     */
    open operator fun plus(itemList: List<T>) {
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(this.itemList.lastIndex, itemList.size)
    }

    /**
     * Add new item to already existing adapter items
     * @param item new adapter item
     */
    open operator fun plus(item: T) {
        itemList.add(item)
        notifyItemInserted(itemList.lastIndex)
    }

    /**
     * Add new item to already existing adapter items
     * @param index index where to insert the item
     * @param item new adapter item
     */
    open fun add(index: Int, item: T) {
        itemList.add(index, item)
        notifyItemInserted(index)
    }

    /**
     * Add new item to already existing adapter items
     * @param items new adapter items
     */
    open fun addAll(items: MutableList<T>) {
        val startPosition = itemList.size
        itemList.addAll(items)
        notifyItemRangeInserted(startPosition, items.size)
    }

    /**
     * Add new item to already existing adapter items
     * @param index index where to insert the item
     * @param items new adapter items
     */
    open fun addAll(index: Int, items: MutableList<T>) {
        itemList.addAll(index, items)
        notifyItemRangeInserted(index, items.size)
    }

    /**
     * Replace only one item on a specific index.
     * @param index index of item form adapter items
     * @param item new adapter item
     */
    open operator fun set(index: Int, item: T) {
        itemList.set(index, item)
        notifyItemInserted(index)
    }

    /**
     * Insert new items from a specific index
     * @param index index of item form adapter items
     * @param itemList new adapter items
     */
    open fun insert(index: Int, itemList: List<T>) {
        this.itemList.addAll(index, itemList)
        notifyItemRangeChanged(index, itemList.size)
    }

    /**
     * Get a item form adapter items
     * @param index index of item form adapter items
     * @return item from adapter list by specified index
     */
    operator fun get(index: Int): T {
        return itemList[index]
    }

    /**
     * Remove a item from adapter items
     * @param index index of item form adapter items
     */
    open operator fun minus(index: Int) {
        itemList.removeAt(index)
        notifyItemRemoved(index)
    }

    /**
     * Remove a item from adapter items
     * @param item item which must been removed
     */
    open operator fun minus(item: T) {
        val indexOfRemovedItem = itemList.indexOf(item)
        itemList.remove(item)
        notifyItemRemoved(indexOfRemovedItem)
    }

    /**
     * Remove all items from adapter
     */
    open fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}