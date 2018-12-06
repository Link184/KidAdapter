package com.link184.kidadapter.typed

import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.simple.SimpleAdapterConfiguration

class MultiAdapterDsl {
    internal val viewTypes = mutableListOf<AdapterViewType<Any>>()
    internal var layoutManager: RecyclerView.LayoutManager? = null
    private val tags = mutableMapOf<String, Int>()

    fun withViewType(tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        val fromPosition = viewTypes.fold(0) { acc, adapterViewType -> acc + adapterViewType.configuration.internalItems.size }
        viewTypes.add(AdapterViewType(fromPosition, block))
        tag?.let { tags.put(it, viewTypes.lastIndex) }
    }

    fun withLayoutManager(block: () -> RecyclerView.LayoutManager?) {
        layoutManager = block()
    }

    /**
     * Useful to build [MultiTypeAdapter] from [SimpleAdapterConfiguration]
     */
    fun fromSimpleConfiguration(block: SimpleAdapterConfiguration<*>.() -> Unit): MultiAdapterDsl {
        return MultiAdapterDsl().apply {
            val adapterConfiguration = SimpleAdapterConfiguration<Any>().apply(block)
            withLayoutManager { adapterConfiguration.layoutManager }
            withViewType {
                withItems(adapterConfiguration.items)
                bind<Any> {
                    adapterConfiguration.bindHolder(this, it)
                }
            }
        }
    }

    internal fun getAllItems(): MutableList<Any> {
        val alignedItems = viewTypes
                .map { it.configuration.internalItems.toMutableList() }
        if (alignedItems.isNotEmpty()) {
            return alignedItems
                    .reduce { acc, items -> acc.apply { addAll(items) } }
        }
        throw IllegalStateException("View types are not defined, at least one must been defined. Use withViewType() method")
    }

    /**
     * Get FIRST mutable list where item type is [T]
     * @param T model type from adapter
     * @return mutable list of [T] items
     */
    internal inline fun <reified T> getItemsByType(): MutableList<T> {
        return viewTypes
            .map { it.configuration.internalItems }
            .first {
                it.any { it is T }
            } as MutableList<T>
    }

    /**
     * Get [AdapterViewType] by given tag.
     */
    internal fun getViewTypeByTag(tag: String): AdapterViewType<Any> {
        tags[tag]?.let { return viewTypes[it] }
        throw IllegalStateException("There are no view types with tag = $tag. Type must been explicitly " +
                "declared with dsl method \"withViewType(TAG, configuration)\"")
    }

    internal fun invalidateItems() {
        val newViewTypes = mutableListOf<AdapterViewType<Any>>()
        viewTypes.forEach { adapterViewType ->
            val fromPosition = newViewTypes.fold(0) { acc, adapterViewType -> acc + adapterViewType.configuration.internalItems.size }
            newViewTypes.add(adapterViewType)
            adapterViewType.positionRange = fromPosition until fromPosition + adapterViewType.configuration.internalItems.size
        }
    }
}