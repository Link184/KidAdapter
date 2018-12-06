package com.link184.kidadapter.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, H : BaseViewHolder<T>>(private var itemList: MutableList<T>) : RecyclerView.Adapter<H>() {

    override fun getItemCount() = itemList.size

    final override fun onBindViewHolder(holder: H, position: Int) {
        holder.bindView(itemList[position])
    }

    operator fun plusAssign(itemList: MutableList<T>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    operator fun plus(itemList: List<T>) {
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    operator fun plus(item: T) {
        itemList.add(item)
        notifyDataSetChanged()
    }

    operator fun set(index: Int, item: T) {
        itemList.add(index, item)
        notifyDataSetChanged()
    }

    fun insert(index: Int, itemList: List<T>) {
        this.itemList.addAll(index, itemList)
        notifyDataSetChanged()
    }

    fun update(item: T, index: Int) {
        itemList[index] = item
        notifyDataSetChanged()
    }

    operator fun get(index: Int): T {
        return itemList[index]
    }

    operator fun minus(index: Int) {
        itemList.removeAt(index)
        notifyDataSetChanged()
    }

    operator fun minus(item: T) {
        itemList.remove(item)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}