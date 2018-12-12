package com.link184.kidadapter.typed

import android.view.View
import androidx.annotation.LayoutRes
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

    inline fun <reified T> withItems(items: MutableList<T>) {
        setInternalItems(items as MutableList<Any>)
        this.modelType = T::class.java
    }

    inline fun <reified T> withItem(item: T) {
        setInternalItems(mutableListOf(item as Any))
        this.modelType = T::class.java
    }

    inline fun <reified T> withEmptyList() {
        setInternalItems(mutableListOf())
        this.modelType = T::class.java
    }

    fun <T> withContentComparator(contentComparator: (T, T) -> Boolean) {
        this.contentComparator = contentComparator as (Any, Any) -> Boolean
    }

    fun <T> withItemsComparator(itemsComparator: (T, T) -> Boolean) {
        this.itemsComparator = itemsComparator as (Any, Any) -> Boolean
    }

    fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }

    fun <T> bind(block: View.(T) -> Unit) {
        bindHolder = (block as View.(Any) -> Unit)
    }

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
        diffCallback = internalItems.clear()
    }

    internal fun getInternalItems() = internalItems
}