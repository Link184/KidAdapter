package com.link184.kidadapter.simple

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapterConfiguration<T> {
    var items = mutableListOf<T>()
        private set
    var layoutManager: RecyclerView.LayoutManager? = null
        private set
    @LayoutRes var layoutResId: Int = -1
        private set
    var bindHolder: View.(T) -> Unit = {}
        private set

    internal fun withItems(items: MutableList<T>) {
        this.items = items
    }

    internal fun withItem(item: T) {
        this.items = mutableListOf(item)
    }

    internal fun withLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
    }

    internal fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }

    fun bind(block: View.(T) -> Unit) {
        this.bindHolder = block
    }
}