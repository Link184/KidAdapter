package com.link184.kidadapter.typed

class UpdateConfiguration {
    val insertionQueue = mutableListOf<Pair<Int, UpdateItem<*>>>()
    val insertTopQueue = mutableListOf<UpdateItem<*>>()
    val insertBottomQueue = mutableListOf<UpdateItem<*>>()
    val replaceQueue = mutableListOf<UpdateItem<*>>()
    val removeQueue = mutableListOf<RemoveItem<*>>()

    inline fun <reified T> insert(index: Int, items: MutableList<T>, tag: String? = null) {
        insertionQueue.add(index to UpdateItem(T::class.java, tag, items))
    }

    inline fun <reified T> insertTop(item: T, tag: String? = null) {
        insertTopQueue.add(UpdateItem(T::class.java, tag, item))
    }

    inline fun <reified T> insertTop(items: MutableList<T>, tag: String? = null) {
        insertTopQueue.add(UpdateItem(T::class.java, tag, items))
    }

    inline fun <reified T> insertBottom(item: T, tag: String? = null) {
        insertBottomQueue.add(UpdateItem(T::class.java, tag, item))
    }

    inline fun <reified T> insertBottom(items: MutableList<T>, tag: String? = null) {
        insertBottomQueue.add(UpdateItem(T::class.java, tag, items))
    }

    inline fun <reified T> replaceItems(items: MutableList<T>, tag: String? = null) {
        replaceQueue.add(UpdateItem(T::class.java, tag, items))
    }

    inline fun <reified T> removeItems(items: MutableList<T>, tag: String? = null) {
        removeQueue.add(RemoveItem(T::class.java, tag, items))
    }

    inline fun <reified T> removeAll(tag: String? = null) {
        removeQueue.add(RemoveItem(modelType = T::class.java, tag = tag))
    }

    internal fun doUpdate(multiAdapterDsl: MultiAdapterDsl) {
        insertionQueue.forEach { insert(it.first, it.second, multiAdapterDsl) }
        insertTopQueue.forEach { insertTop(it, multiAdapterDsl) }
        insertBottomQueue.forEach { insertBottom(it, multiAdapterDsl) }
        replaceQueue.forEach { replaceItems(it, multiAdapterDsl) }
        removeQueue.forEach { removeItems(it, multiAdapterDsl) }
        multiAdapterDsl.invalidateItems()
    }

    private fun insert(index: Int, item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems.addAll(index, item.items as List<Any>)
    }

    private fun insertTop(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems
                .addAll(0, item.items as List<Any>)
    }

    private fun insertBottom(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems
                .addAll(item.items as List<Any>)
    }

    private fun replaceItems(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems = item.items as MutableList<Any>
    }

    private fun removeItems(item: RemoveItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item.toUpdateItem(), multiAdapterDsl)
        if (item.items.isNotEmpty()) {
            viewType.configuration.internalItems.removeAll(item.items)
        } else {
            viewType.configuration.internalItems.clear()
        }
    }

    private fun <T> getAdapterViewTypeByType(item: UpdateItem<T>, multiAdapterDsl: MultiAdapterDsl): AdapterViewType<Any> {
        item.tag?.let {
            return multiAdapterDsl.getViewTypeByTag(it)
        }

        val itemIsPresent = multiAdapterDsl.viewTypes.any { it.configuration.modelType == item.modelType }
        if (!itemIsPresent) {
            throw IllegalStateException("Sorry but ${item.modelType} isn't declared as a view type. " +
                    "You try to update non-existent view type, you can update only declared view types, " +
                    "please declare view type with withViewType() on adapter creation time method before update it")
        }
        return multiAdapterDsl.viewTypes.first { it.configuration.modelType == item.modelType }
    }
}