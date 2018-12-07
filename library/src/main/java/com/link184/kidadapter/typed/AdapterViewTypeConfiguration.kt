package com.link184.kidadapter.typed

import android.view.View
import androidx.annotation.LayoutRes

class AdapterViewTypeConfiguration {
    var internalItems: MutableList<Any> = mutableListOf(Unit)
    @LayoutRes var layoutResId: Int = -1
        private set
    internal var bindHolder: View.(Any) -> Unit = {}
        private set
    var modelType: Class<*>? = null

    inline fun <reified T> withItems(items: MutableList<T>) {
        this.internalItems = items as MutableList<Any>
        this.modelType = T::class.java
    }

    inline fun <reified T> withItem(item: T) {
        this.internalItems = mutableListOf(item as Any)
        this.modelType = T::class.java
    }

    inline fun <reified T> withEmptyList() {
        this.internalItems = mutableListOf()
        this.modelType = T::class.java
    }

    fun withLayoutResId(@LayoutRes layoutResId: Int) {
        this.layoutResId = layoutResId
    }
    fun <T> bind(block: View.(T) -> Unit)  {
        bindHolder = (block as View.(Any) -> Unit)
    }
}