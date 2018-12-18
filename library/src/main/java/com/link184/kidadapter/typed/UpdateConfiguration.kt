package com.link184.kidadapter.typed

import com.link184.kidadapter.base.KidDiffUtilCallback
import com.link184.kidadapter.exceptions.UndeclaredTypeModification
import com.link184.kidadapter.exceptions.WrongTagType

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

    internal fun doUpdate(typedKidAdapterConfiguration: TypedKidAdapterConfiguration): MutableList<KidDiffUtilCallback<*>?> {
        val diffCallbacks = mutableListOf<KidDiffUtilCallback<*>?>()
        updateQueue.forEach {
            when (it.updateType) {
                is UpdateType.Insert -> insert(it, typedKidAdapterConfiguration).let(diffCallbacks::add)
                is UpdateType.ReplaceAll -> replaceItems(it, typedKidAdapterConfiguration).let(diffCallbacks::add)
                is UpdateType.Remove -> removeItems(it, typedKidAdapterConfiguration).let(diffCallbacks::add)
            }
        }
        typedKidAdapterConfiguration.invalidateItems()
        return diffCallbacks
    }

    private fun insert(item: UpdateItem<*>, typedKidAdapterConfiguration: TypedKidAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        viewType.configuration.addAllToInternalItems(item.updateType.resolveIndex(viewType.configuration.getInternalItems().newList), item.items as MutableList<Any>)
        return viewType.configuration.diffCallback
    }

    private fun replaceItems(item: UpdateItem<*>, typedKidAdapterConfiguration: TypedKidAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        viewType.configuration.setInternalItems(item.items as MutableList<Any>)
        return viewType.configuration.diffCallback
    }

    private fun removeItems(item: UpdateItem<*>, typedKidAdapterConfiguration: TypedKidAdapterConfiguration): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        if (item.items.isNotEmpty()) {
            viewType.configuration.removeAllInternalItems(item.items as MutableList<Any>)
        } else {
            viewType.configuration.clearInternalItems()
        }
        return viewType.configuration.diffCallback
    }

    private fun <T> getAdapterViewTypeByType(item: UpdateItem<T>, typedKidAdapterConfiguration: TypedKidAdapterConfiguration): AdapterViewType<Any> {
        item.tag?.let {
            return typedKidAdapterConfiguration.getViewTypeByTag(it).also { byTypeItem ->
                if (byTypeItem.configuration.modelType != item.modelType) {
                    throw WrongTagType(it)
                }
            }
        }

        val itemIsPresent = typedKidAdapterConfiguration.viewTypes.any { it.configuration.modelType == item.modelType }
        if (!itemIsPresent) {
            throw UndeclaredTypeModification(item.modelType)
        }
        return typedKidAdapterConfiguration.viewTypes.first { it.configuration.modelType == item.modelType }
    }
}