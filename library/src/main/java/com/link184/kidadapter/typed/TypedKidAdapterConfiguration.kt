package com.link184.kidadapter.typed

import android.support.v7.widget.RecyclerView
import com.link184.kidadapter.ConfigurationDsl
import com.link184.kidadapter.exceptions.UndeclaredTag
import com.link184.kidadapter.exceptions.UndefinedLayout
import com.link184.kidadapter.exceptions.ZeroViewTypes
import com.link184.kidadapter.simple.SingleKidAdapterConfiguration

/**
 * Typed Adapter configuration holder.
 */
class TypedKidAdapterConfiguration {
    internal val viewTypes = mutableListOf<AdapterViewType<Any>>()
    internal var layoutManager: RecyclerView.LayoutManager? = null

    /**
     * Declare adapter view type.
     * @param tag each view type can be associated with unique string value. Useful when you adapter contains multiple
     * view types with the same data type, in this case you can easily update desired view type by tag.
     * @param block configure your view type here.
     */
    @ConfigurationDsl
    fun withViewType(tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        val fromPosition =
            viewTypes.fold(0) { acc, adapterViewType -> acc + adapterViewType.configuration.getInternalItems().size }
        viewTypes.add(AdapterViewType(tag, fromPosition, block))
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
     * Useful to build [TypedKidAdapter] from [SingleKidAdapterConfiguration]
     * @param block configure your single adapter configuration here
     * @return typed adapter configuration transformed from single adapter configuration
     */
    @ConfigurationDsl
    fun fromSimpleConfiguration(block: SingleKidAdapterConfiguration<*>.() -> Unit): TypedKidAdapterConfiguration {
        return TypedKidAdapterConfiguration().apply {
            val adapterConfiguration = SingleKidAdapterConfiguration<Any>().apply(block)
            withLayoutManager { adapterConfiguration.layoutManager }
            withViewType {
                withItems(adapterConfiguration.items.newList)
                bind<Any> {
                    adapterConfiguration.bindHolder(this, it)
                }
            }
        }
    }

    internal fun getAllItems(): MutableList<Any> {
        val alignedItems = viewTypes
            .map { it.configuration.getInternalItems().toMutableList() }
        if (alignedItems.isNotEmpty()) {
            return alignedItems
                .reduce { acc, items -> acc.apply { addAll(items) } }
        }
        throw ZeroViewTypes()
    }

    /**
     * Get FIRST mutable list where item type is [T]
     * @param T model type from adapter
     * @return mutable list of [T] items
     */
    internal inline fun <reified T> getItemsByType(): MutableList<T> {
        return viewTypes
            .map { it.configuration.getInternalItems().newList }
            .first {
                it.any { it is T }
            } as MutableList<T>
    }

    /**
     * Get [AdapterViewType] by given tag.
     */
    internal fun getViewTypeByTag(tag: String): AdapterViewType<Any> {
        viewTypes
            .filter { it.tag != null }
            .firstOrNull { it.tag == tag }
            ?.let { return it }
        throw UndeclaredTag(tag)
    }

    internal fun invalidateItems() {
        val newViewTypes = mutableListOf<AdapterViewType<Any>>()
        viewTypes.forEach { adapterViewType ->
            val fromPosition =
                newViewTypes.fold(0) { acc, adapterViewType -> acc + adapterViewType.configuration.getInternalItems().size }
            newViewTypes.add(adapterViewType)
            val toPosition = fromPosition + adapterViewType.configuration.getInternalItems().size
            adapterViewType.positionRange = fromPosition until if (toPosition < 1) 1 else toPosition
        }
    }

    internal fun validate() {
        when {
            viewTypes.firstOrNull { it.configuration.layoutResId == -1 } != null -> throw UndefinedLayout(
                "Adapter layout is not set, please declare it for each AdapterViewType with withLayoutResId() function"
            )
        }
    }
}