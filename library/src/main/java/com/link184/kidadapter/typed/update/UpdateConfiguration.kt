package com.link184.kidadapter.typed.update

import com.link184.kidadapter.base.KidDiffUtilCallback
import com.link184.kidadapter.exceptions.UndeclaredTypeModification
import com.link184.kidadapter.exceptions.WrongTagType
import com.link184.kidadapter.typed.AdapterViewType
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

class UpdateConfiguration {
    val updateQueue = mutableListOf<UpdateItem<*>>()

    /**
     * Insert items from specific index into already declared view typed list.
     * @param index index from where items should been inserted
     * @param items a mutable list which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insert(index: Int, items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertMiddle(index)))
    }

    /**
     * Insert a item from specific index into already declared view typed list.
     * @param index index from where items should been inserted
     * @param item a item which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insert(index: Int, item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertMiddle(index)))
    }

    /**
     * Insert a item from index 0 into already declared view typed list.
     * @param item a item which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insertTop(item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertTop))
    }

    /**
     * Insert items from index 0 into already declared view typed list.
     * @param items a mutable list which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insertTop(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertTop))
    }

    /**
     * Insert a item from last index into already declared view typed list.
     * @param item a item which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insertBottom(item: T, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, item, UpdateType.Insert.InsertBottom))
    }

    /**
     * Insert items from last index into already declared view typed list.
     * @param items a mutable list which should been inserted.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> insertBottom(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Insert.InsertBottom))
    }

    /**
     * Replace ALL items form already declared list.
     * @param items a mutable list which should been replaced.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> replaceAllItems(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.ReplaceAll))
    }

    /**
     * Remove items from already declared list.
     * @param items a mutable list which should been removed.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
    inline fun <reified T> removeItems(items: MutableList<T>, tag: String? = null) {
        updateQueue.add(UpdateItem(T::class.java, tag, items, UpdateType.Remove))
    }

    /**
     * Remove all items from already declared list.
     * @param tag nullable tag. It must been declared on adapter initialization.
     */
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

    private fun insert(
        item: UpdateItem<*>,
        typedKidAdapterConfiguration: TypedKidAdapterConfiguration
    ): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        viewType.configuration.addAllToInternalItems(
            item.updateType.resolveIndex(viewType.configuration.getInternalItems().newList),
            item.items as MutableList<Any>
        )
        return viewType.configuration.diffCallback
    }

    private fun replaceItems(
        item: UpdateItem<*>,
        typedKidAdapterConfiguration: TypedKidAdapterConfiguration
    ): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        viewType.configuration.setInternalItems(item.items as MutableList<Any>)
        return viewType.configuration.diffCallback
    }

    private fun removeItems(
        item: UpdateItem<*>,
        typedKidAdapterConfiguration: TypedKidAdapterConfiguration
    ): KidDiffUtilCallback<Any>? {
        val viewType = getAdapterViewTypeByType(item, typedKidAdapterConfiguration)
        if (item.items.isNotEmpty()) {
            viewType.configuration.removeAllInternalItems(item.items as MutableList<Any>)
        } else {
            viewType.configuration.clearInternalItems()
        }
        return viewType.configuration.diffCallback
    }

    private fun <T> getAdapterViewTypeByType(
        item: UpdateItem<T>,
        typedKidAdapterConfiguration: TypedKidAdapterConfiguration
    ): AdapterViewType<Any> {
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