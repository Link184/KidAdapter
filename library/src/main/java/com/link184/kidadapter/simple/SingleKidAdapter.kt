package com.link184.kidadapter.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.base.BaseAdapter
import com.link184.kidadapter.base.BaseViewHolder
import com.link184.kidadapter.base.KidDiffUtilCallback

open class SingleKidAdapter<T>(private val configuration: SingleKidAdapterConfiguration<T>) :
    BaseAdapter<T, BaseViewHolder<T>>(configuration.items) {
    init {
        configuration.validate()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(configuration.layoutResId, parent, false)
        val viewHolder = object : BaseViewHolder<T>(view) {
            override fun bindView(item: T) {
                configuration.bindHolderIndexed(itemView, item, adapterPosition)
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

    override operator fun plusAssign(itemList: MutableList<T>) {
        this.itemList.reset(itemList)
        notifyDataSetChanged()
    }

    override operator fun plus(itemList: List<T>): BaseAdapter<T, BaseViewHolder<T>> {
        this.itemList.addAll(itemList).also(::dispatchUpdates)
        return this
    }

    override operator fun plus(item: T): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.add(item).also(::dispatchUpdates)
        return this
    }

    override fun add(index: Int, item: T): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.add(index, item).also(::dispatchUpdates)
        return this
    }

    override fun addAll(items: MutableList<T>): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.addAll(items).also(::dispatchUpdates)
        return this
    }

    override fun addAll(index: Int, items: MutableList<T>): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.addAll(index, items).also(::dispatchUpdates)
        return this
    }

    override operator fun set(index: Int, item: T) {
        itemList.set(index, item).also(::dispatchUpdates)
    }

    override fun insert(index: Int, itemList: List<T>): BaseAdapter<T, BaseViewHolder<T>> {
        this.itemList.addAll(index, itemList).also(::dispatchUpdates)
        return this
    }

    override fun remove(index: Int): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.removeAt(index).also(::dispatchUpdates)
        return this
    }

    override operator fun minus(item: T): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.remove(item).also(::dispatchUpdates)
        return this
    }

    override fun clear(): BaseAdapter<T, BaseViewHolder<T>> {
        itemList.clear().also(::dispatchUpdates)
        return this
    }

    private fun dispatchUpdates(diffUtilCallback: KidDiffUtilCallback<T>) {
        with(diffUtilCallback) {
            contentComparator = configuration.contentComparator
            itemsComparator = configuration.itemsComparator
            DiffUtil.calculateDiff(this).dispatchUpdatesTo(this@SingleKidAdapter)
        }
    }
}