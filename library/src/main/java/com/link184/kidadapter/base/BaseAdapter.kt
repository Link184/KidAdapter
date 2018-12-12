package com.link184.kidadapter.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, H : BaseViewHolder<T>>(protected var itemList: KidList<T>) : RecyclerView.Adapter<H>() {
    constructor(itemList: MutableList<T>) : this(KidList(itemList))

    override fun getItemCount() = itemList.size

    final override fun onBindViewHolder(holder: H, position: Int) {
        holder.bindView(itemList[position])
    }

    operator fun plusAssign(itemList: MutableList<T>) {
        this.itemList.reset(itemList)
    }

    operator fun plus(itemList: List<T>) {
        this.itemList.addAll(itemList)
        notifyItemRangeInserted(this.itemList.lastIndex, itemList.size)
    }

    operator fun plus(item: T) {
        itemList.add(item)
        notifyItemInserted(itemList.lastIndex)
    }

    operator fun set(index: Int, item: T) {
        itemList.add(index, item)
        notifyItemInserted(index)
    }

    fun insert(index: Int, itemList: List<T>) {
        this.itemList.addAll(index, itemList)
        notifyItemRangeChanged(index, itemList.size)
    }

    fun update(item: T, index: Int) {
        itemList.set(index, item)
        notifyItemChanged(index)
    }

    operator fun get(index: Int): T {
        return itemList[index]
    }

    operator fun minus(index: Int) {
        itemList.removeAt(index)
        notifyItemRemoved(index)
    }

    operator fun minus(item: T) {
        val indexOfRemovedItem = itemList.indexOf(item)
        itemList.remove(item)
        notifyItemRemoved(indexOfRemovedItem)
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}