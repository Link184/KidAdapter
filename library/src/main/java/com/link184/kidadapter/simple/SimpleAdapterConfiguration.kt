package com.link184.kidadapter.simple

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapterConfiguration<T> {
    internal var items = mutableListOf<T>()
        private set
    internal var layoutManager: RecyclerView.LayoutManager? = null
        private set
    @LayoutRes internal var layoutResId: Int = -1
        private set
    internal var bindHolder: View.(T) -> Unit = {}
        private set

    fun withItems(items: MutableList<T>) {
        this.items = items
    }

    fun withItem(item: T) {
        this.items = mutableListOf(item)
    }

    fun withLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
    }

    fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }

    fun bind(block: View.(T) -> Unit) {
        this.bindHolder = block
    }
}