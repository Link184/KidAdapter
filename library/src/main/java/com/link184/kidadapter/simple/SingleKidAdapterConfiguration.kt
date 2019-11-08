package com.link184.kidadapter.simple

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
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
    internal var viewInitializer: (Context.() -> View)? = null
        private set
    internal var bindHolderIndexed: View.(T, Int) -> Unit = { item, i -> }
        private set
    internal var bindHolder: View.(T) -> Unit = { }
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
     */
    @ConfigurationDsl
    fun withLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
    }

    /**
     * Set [RecyclerView.LayoutManager] of a current [RecyclerView].
     * By default it is a vertical [LinearLayoutManager]
     * @param block configure your layout manager here
     */
    @ConfigurationDsl
    fun withLayoutManager(block: () -> RecyclerView.LayoutManager?) {
        layoutManager = block()
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
     * Set view which will be bounded to actual view type
     * @param viewInitializer view initialization lambda function.
     * Initialize your view with [Context] from this lambda.
     */
    @ConfigurationDsl
    fun withLayoutView(viewInitializer: Context.() -> View) {
        this.viewInitializer = viewInitializer
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
     * @param block.item item from adapter list at adapter position, equivalent of itemsList.get(adapterPosition]
     * @param block.index adapterPosition
     */
    @BindDsl
    fun bindIndexed(block: View.(item: T, index: Int) -> Unit) {
        this.bindHolderIndexed = block
    }

    /**
     * Set action which must been called when [ecyclerView.Adapter.onBindViewHolder]
     * @param block is executed in [RecyclerView.ViewHolder.itemView] context
     * @param block.item item from adapter list at adapter position, equivalent of itemsList.get(adapterPosition]
     */
    @BindDsl
    fun bind(block: View.(T) -> Unit) {
        this.bindHolder = block
    }

    internal fun validate() {
        when {
            layoutResId == -1 && viewInitializer == null && items.isNotEmpty() -> throw UndefinedLayout(
                "Adapter layout is not set, " +
                        "please declare it with withLayoutResId() or withLayoutView() function"
            )
            viewInitializer != null && layoutResId != -1 -> throw UndefinedLayout(
                "The layout is defined through both functions (withLayoutResId and withLayoutView)" +
                        "can`t decide which layout to pick"
            )
        }
    }
}