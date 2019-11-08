package com.link184.kidadapter.typed

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.link184.kidadapter.BindDsl
import com.link184.kidadapter.ConfigurationDsl
import com.link184.kidadapter.base.KidDiffUtilCallback
import com.link184.kidadapter.base.KidList
import com.link184.kidadapter.exceptions.UndefinedLayout

class AdapterViewTypeConfiguration {
    private var internalItems: KidList<Any> = KidList()
    @LayoutRes
    var layoutResId: Int = -1
        private set
    var viewInitializer: (Context.() -> View)? = null
        private set
    internal var bindHolderIndexed: View.(Any, Int) -> Unit = { _, _ -> }
    internal var bindHolder: View.(Any) -> Unit = { }
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
    @ConfigurationDsl
    inline fun <reified T> withItems(items: MutableList<T>) {
        setInternalItems(items as MutableList<Any>)
        this.modelType = T::class.java
    }

    /**
     * Set adapter view type item here.
     * @param item item to be inserted in RecyclerView
     */
    @ConfigurationDsl
    inline fun <reified T> withItem(item: T) {
        setInternalItems(mutableListOf(item as Any))
        this.modelType = T::class.java
    }

    /**
     * Set adapter view type initialization with empty list. Is mandatory to call because KidAdapter must know all
     * data types for future items update (in runtime)
     */
    @ConfigurationDsl
    inline fun <reified T> withEmptyList() {
        setInternalItems(mutableListOf())
        this.modelType = T::class.java
    }

    /**
     * Equivalent to [DiffUtil.Callback.areContentsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */
    @ConfigurationDsl
    fun <T> withContentComparator(contentComparator: (T, T) -> Boolean) {
        this.contentComparator = contentComparator as (Any, Any) -> Boolean
    }

    /**
     * Equivalent to [DiffUtil.Callback.areItemsTheSame]. Method call is optional, by default in compare objects
     * by equals, if you want another behavior then please implement it here.
     */
    @ConfigurationDsl
    fun <T> withItemsComparator(itemsComparator: (T, T) -> Boolean) {
        this.itemsComparator = itemsComparator as (Any, Any) -> Boolean
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
     * Set action which must been called when [ecyclerView.Adapter.onBindViewHolder]
     * @param block is executed in [RecyclerView.ViewHolder.itemView] context
     * @param block.item item from adapter list at adapter position, equivalent of itemsList.get(adapterPosition]
     * @param block.index adapterPosition
     */
    @BindDsl
    fun <T> bindIndexed(block: View.(T, Int) -> Unit) {
        bindHolderIndexed = (block as View.(Any, Int) -> Unit)
    }
    /**
     * Set action which must been called when [ecyclerView.Adapter.onBindViewHolder]
     * @param block is executed in [RecyclerView.ViewHolder.itemView] context
     * @param block.item item from adapter list at adapter position, equivalent of itemsList.get(adapterPosition]
     */
    @BindDsl
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

    internal fun swapInternalItems(firstIndex: Int, secondIndex: Int) {
        diffCallback = internalItems.swap(firstIndex, secondIndex)
    }

    internal fun getInternalItems() = internalItems

    internal fun validate() {
        when {
            layoutResId == -1 && viewInitializer == null && internalItems.isNotEmpty() -> throw UndefinedLayout(
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