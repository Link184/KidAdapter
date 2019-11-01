package com.link184.kidadapter.base

import androidx.recyclerview.widget.RecyclerView
/* ktlint-disable no-wildcard-imports */
import java.util.*

/* ktlint-enable no-wildcard-imports */

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
    open operator fun plus(itemList: List<T>): BaseAdapter<T, H> {
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(this.itemList.lastIndex, itemList.size)
        return this
    }

    /**
     * Add new item to already existing adapter items
     * @param item new adapter item
     */
    open operator fun plus(item: T): BaseAdapter<T, H> {
        itemList.add(item)
        notifyItemInserted(itemList.lastIndex)
        return this
    }

    /**
     * Add new item to already existing adapter items
     * @param index index where to insert the item
     * @param item new adapter item
     */
    open fun add(index: Int, item: T): BaseAdapter<T, H> {
        itemList.add(index, item)
        notifyItemInserted(index)
        return this
    }

    /**
     * Add new item to already existing adapter items
     * @param items new adapter items
     */
    open fun addAll(items: MutableList<T>): BaseAdapter<T, H> {
        val startPosition = itemList.size
        itemList.addAll(items)
        notifyItemRangeInserted(startPosition, items.size)
        return this
    }

    /**
     * Add new item to already existing adapter items
     * @param index index where to insert the item
     * @param items new adapter items
     */
    open fun addAll(index: Int, items: MutableList<T>): BaseAdapter<T, H> {
        itemList.addAll(index, items)
        notifyItemRangeInserted(index, items.size)
        return this
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
    open fun insert(index: Int, itemList: List<T>): BaseAdapter<T, H> {
        this.itemList.addAll(index, itemList)
        notifyItemRangeChanged(index, itemList.size)
        return this
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
    open fun remove(index: Int): BaseAdapter<T, H> {
        itemList.removeAt(index)
        notifyItemRemoved(index)
        return this
    }

    /**
     * Remove a item from adapter items
     * @param item item which must been removed
     */
    open operator fun minus(item: T): BaseAdapter<T, H> {
        val indexOfRemovedItem = itemList.indexOf(item)
        itemList.remove(item)
        notifyItemRemoved(indexOfRemovedItem)
        return this
    }

    /**
     * Remove all items from adapter
     */
    open fun clear(): BaseAdapter<T, H> {
        itemList.clear()
        notifyDataSetChanged()
        return this
    }

    /**
     * Swaps 2 items between them
     * @param firstIndex first index to swap
     * @param secondIndex second index to swap
     */
    open fun swap(firstIndex: Int, secondIndex: Int): BaseAdapter<T, H> {
        Collections.swap(itemList, firstIndex, secondIndex)
        notifyItemChanged(firstIndex)
        notifyItemChanged(secondIndex)
        return this
    }

    /**
     * Get copy of items in a non mutable list
     * Changes on result list will not affect the items from adater.
     */
    fun getAllItems(): List<T> = itemList.toList()
}