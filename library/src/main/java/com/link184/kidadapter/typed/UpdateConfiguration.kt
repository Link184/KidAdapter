package com.link184.kidadapter.typed

class UpdateConfiguration {
    val updateQueue = mutableListOf<UpdateItem<*>>()

    inline fun <reified T> insert(index: Int, items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertMiddle(index)))
    }

    inline fun <reified T> insertTop(item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertTop))
    }

    inline fun <reified T> insertTop(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertTop))
    }

    inline fun <reified T> insertBottom(item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertBottom))
    }

    inline fun <reified T> insertBottom(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertBottom))
    }

    inline fun <reified T> replaceAllItems(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.ReplaceAll))
    }

    inline fun <reified T> removeItems(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Remove))
    }

    inline fun <reified T> removeAll(tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, mutableListOf(), UpdateType.Remove))
    }

    internal fun doUpdate(multiAdapterDsl: MultiAdapterDsl) {
        updateQueue.forEach {
            when (it.updateType) {
                is UpdateType.Insert -> insert(it, multiAdapterDsl)
                is UpdateType.ReplaceAll -> replaceItems(it, multiAdapterDsl)
                is UpdateType.Remove -> removeItems(it, multiAdapterDsl)
            }
        }
        multiAdapterDsl.invalidateItems()
    }

    private fun insert(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems.addAll(item.updateType.resolveIndex(item.items), item.items as List<Any>)
    }

    private fun replaceItems(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
        viewType.configuration.internalItems = item.items as MutableList<Any>
    }

    private fun removeItems(item: UpdateItem<*>, multiAdapterDsl: MultiAdapterDsl) {
        val viewType = getAdapterViewTypeByType(item, multiAdapterDsl)
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