package com.link184.kidadapter.typed

import android.support.annotation.LayoutRes
import android.view.View
import com.link184.kidadapter.base.KidDiffUtilCallback
import com.link184.kidadapter.base.KidList

class AdapterViewTypeConfiguration {
    private var internalItems: KidList<Any> = KidList()
    @LayoutRes
    var layoutResId: Int = -1
        private set
    internal var bindHolder: View.(Any) -> Unit = {}
        private set
    var modelType: Class<*>? = null
    private var contentComparator: ((Any, Any) -> Boolean)? = null
    private var itemsComparator: ((Any, Any) -> Boolean)? = null
    internal var diffCallback: KidDiffUtilCallback<Any>? = null
        get() = field?.apply {
            this.contentComparator = this@AdapterViewTypeConfiguration.contentComparator
            this.itemsComparator = this@AdapterViewTypeConfiguration.itemsComparator
        }

    /**
     * Set adapter view type items here.
     * @param items items to be inserted in RecyclerView.
     */
    inline fun <reified T> withItems(items: MutableList<T>) {
        setInternalItems(items as MutableList<Any>)
        this.modelType = T::class.java
    }

    /**
     * Set adapter view type item here.
     * @param item item to be inserted in RecyclerView
     */
    inline fun <reified T> withItem(item: T) {
        setInternalItems(mutableListOf(item as Any))
        this.modelType = T::class.java
    }

    /**
     * Set adapter view type initialization with empty list. Is mandatory to call because KidAdapter must know all
     * data types for future items update (in runtime)
     */
    inline fun <reified T> withEmptyList() {
        setInternalItems(mutableListOf())
        this.modelType = T::class.java
    }

    /**
     * Equivalent to [DiffUtil.Callback.areContentsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */
    fun <T> withContentComparator(contentComparator: (T, T) -> Boolean) {
        this.contentComparator = contentComparator as (Any, Any) -> Boolean
    }

    /**
     * Equivalent to [DiffUtil.Callback.areItemsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */

    fun <T> withItemsComparator(itemsComparator: (T, T) -> Boolean) {
        this.itemsComparator = itemsComparator as (Any, Any) -> Boolean
    }

    /**
     * Set layout resource id which will be bounded to actual view type
     * @param layoutResId desired layout resource id
     */
    fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }

    /**
     * Set action which must been called when [ecyclerView.Adapter.onBindViewHolder]
     * @param block is executed in [RecyclerView.ViewHolder.itemView] context
     */
    fun <T> bind(block: View.(T) -> Unit) {
        bindHolder = (block as View.(Any) -> Unit)
    }

    /** INGORE IT */
    fun setInternalItems(items: MutableList<Any>) {
        diffCallback = this.internalItems.reset(items)
    }

    internal fun addAllToInternalItems(index: Int, items: MutableList<Any>) {
        diffCallback = internalItems.addAll(index, items)
    }

    internal fun removeAllInternalItems(items: MutableList<Any>) {
        diffCallback = internalItems.removeAll(items)
    }

    internal fun clearInternalItems() {
        internalItems.clear()
    }

    internal fun getInternalItems() = internalItems
}