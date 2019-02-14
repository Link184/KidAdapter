package com.link184.kidadapter.simple

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.link184.kidadapter.BindDsl
import com.link184.kidadapter.ConfigurationDsl
import com.link184.kidadapter.base.KidList
import com.link184.kidadapter.exceptions.UndefinedLayout

class SingleKidAdapterConfiguration<T> {
    internal var items = KidList<T>()
        private set
    internal var layoutManager: RecyclerView.LayoutManager? = null
        private set
    @LayoutRes
    internal var layoutResId: Int = -1
        private set
    internal var bindHolder: View.(T,Int) -> Unit = {item, i ->  }
        private set
    internal var contentComparator: ((T, T) -> Boolean)? = null
    internal var itemsComparator: ((T, T) -> Boolean)? = null

    /**
     * Set adapter view type items here.
     * @param items items to be inserted in RecyclerView.
     */
    @ConfigurationDsl
    fun withItems(items: MutableList<T>) {
        this.items.reset(items)
    }

    /**
     * Set adapter view type item here.
     * @param item item to be inserted in RecyclerView
     */
    @ConfigurationDsl
    fun withItem(item: T) {
        this.items.reset(mutableListOf(item))
    }

    /**
     * Set [androidx.recyclerview.widget.RecyclerView.LayoutManager] of a current [RecyclerView].
     * By default it is a vertical [androidx.recyclerview.widget.LinearLayoutManager]
     * @param block configure your layout manager here
     */
    @ConfigurationDsl
    fun withLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
    }

    /**
     * Set layout resource id which will be bounded to actual view type
     * @param layoutResId desired layout resource id
     */
    @ConfigurationDsl
    fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }

    /**
     * Equivalent to [DiffUtil.Callback.areContentsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */
    @ConfigurationDsl
    fun withContentComparator(contentComparator: (T, T) -> Boolean) {
        this.contentComparator = contentComparator
    }

    /**
     * Equivalent to [DiffUtil.Callback.areItemsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */
    @ConfigurationDsl
    fun withItemsComparator(itemsComparator: (T, T) -> Boolean) {
        this.itemsComparator = itemsComparator
    }

    /**
     * Set action which must been called when [ecyclerView.Adapter.onBindViewHolder]
     * @param block is executed in [RecyclerView.ViewHolder.itemView] context
     */
    @BindDsl
    fun bind(block: View.(T,Int) -> Unit) {
        this.bindHolder = block
    }

    internal fun validate() {
        when {
            layoutResId == -1 && items.isNotEmpty() -> throw UndefinedLayout("Adapter layout is not set, " +
                    "please declare it with withLayoutResId() function")
        }
    }
}