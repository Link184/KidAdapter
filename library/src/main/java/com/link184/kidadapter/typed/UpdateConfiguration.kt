package com.link184.kidadapter.typed

import com.link184.kidadapter.base.KidDiffUtilCallback

class UpdateConfiguration {
    val updateQueue = mutableListOf<UpdateItem<*>>()

    inline fun <reified T> insert(index: Int, items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertMiddle(index)))
    }

    inline fun <reified T> insert(index: Int, item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertMiddle(index)))
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

    internal fun doUpdate(multiAdapterConfiguration: MultiAdapterConfiguration): MutableList<KidDiffUtilCallback<*>?> {
        val diffCallbacks = mutableListOf<KidDiffUtilCallback<*>?>()
        updateQueue.forEach {
            when (it.updateType) {
                is UpdateType.Insert -> insert(it, multiAdapterConfiguration).let(diffCallbacks::add)
                is UpdateType.ReplaceAll -> replaceItems(it, multiAdapterConfiguration).let(diffCallbacks::add)
                is UpdateType.Remove -> removeItems(it, multiAdapterConfiguration).let(diffCallbacks::add)
            }
        }
        multiAdapterConfiguration.invalidateItems()
        return diffCallbacks
    }

    private fun insert(item: UpdateItem<*>, multiAdapterConfiguration: MultiAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, multiAdapterConfiguration)
        viewType.configuration.addAllToInternalItems(item.updateType.resolveIndex(viewType.configuration.getInternalItems().newList), item.items as MutableList<Any>)
        return viewType.configuration.diffCallback
    }

    private fun replaceItems(item: UpdateItem<*>, multiAdapterConfiguration: MultiAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, multiAdapterConfiguration)
        viewType.configuration.setInternalItems(item.items as MutableList<Any>)
        return viewType.configuration.diffCallback
    }

    private fun removeItems(item: UpdateItem<*>, multiAdapterConfiguration: MultiAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, multiAdapterConfiguration)
        if (item.items.isNotEmpty()) {
            viewType.configuration.removeAllInternalItems(item.items as MutableList<Any>)
        } else {
            viewType.configuration.clearInternalItems()
        }
        return viewType.configuration.diffCallback
    }

    private fun <T> getAdapterViewTypeByType(item: UpdateItem<T>, multiAdapterConfiguration: MultiAdapterConfiguration): AdapterViewType<Any> {
        item.tag?.let {
            return multiAdapterConfiguration.getViewTypeByTag(it)
        }

        val itemIsPresent = multiAdapterConfiguration.viewTypes.any { it.configuration.modelType == item.modelType }
        if (!itemIsPresent) {
            throw IllegalStateException("Sorry but ${item.modelType} isn't declared as a view type. " +
                    "You try to update non-existent view type, you can update only declared view types, " +
                    "please declare view type with withViewType() on adapter creation time method before update it")
        }
        return multiAdapterConfiguration.viewTypes.first { it.configuration.modelType == item.modelType }
    }
}