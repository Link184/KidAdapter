package com.link184.kidadapter.typed.restructure

import com.link184.kidadapter.ConfigurationDsl
import com.link184.kidadapter.exceptions.UndeclaredTag
import com.link184.kidadapter.exceptions.ZeroViewTypes
import com.link184.kidadapter.typed.AdapterViewType
import com.link184.kidadapter.typed.AdapterViewTypeConfiguration
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

class RestructureConfiguration {
    private val restructureQueue = mutableListOf<RestructureItem>()

    /**
     * Declare new [AdapterViewType] at a specific index
     * @param index index where new [AdapterViewType] must been inserted
     * @param newTag nullable tag. New [AdapterViewType] can be marked with a tag for future comfortable manipulation.
     * @param block configuration of a new [AdapterViewType]
     *
     * @throws IndexOutOfBoundsException in case of invalid index
     */
    @ConfigurationDsl
    fun insert(index: Int, newTag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(newTag, block, RestructureType.Insert.InsertMiddle(index)))
    }

    /**
     * Declare new [AdapterViewType] at top of all view types
     * @param newTag nullable tag. New [AdapterViewType] can be marked with a newTag for future comfortable manipulation.
     * @param block configuration of a new [AdapterViewType]
     *
     * @throws UndeclaredTag in case of invalid tag
     */
    @ConfigurationDsl
    fun insertTop(newTag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(newTag, block, RestructureType.Insert.InsertTop))
    }

    /**
     * Declare new [AdapterViewType] at bottom of all view types
     * @param newTag nullable tag. New [AdapterViewType] can be marked with a tag for future comfortable manipulation.
     * @param block configuration of a new [AdapterViewType]
     *
     * @throws UndeclaredTag in case of invalid tag
     */
    @ConfigurationDsl
    fun insertBottom(newTag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(newTag, block, RestructureType.Insert.InsertBottom))
    }

    /**
     * Remove [AdapterViewType] by tag
     * @param tag Tag which was associated to [AdapterViewType] when it was declared.
     * @throws UndeclaredTag in case of invalid tag
     */
    @ConfigurationDsl
    fun remove(tag: String) {
        restructureQueue.add(RestructureItem(tag, { }, RestructureType.Remove(-1)))
    }

    /**
     * Remove [AdapterViewType] by index
     * @param index [AdapterViewType] position from where it must been removed
     *
     * @throws IndexOutOfBoundsException in case of invalid index
     */
    @ConfigurationDsl
    fun remove(index: Int) {
        restructureQueue.add(RestructureItem(null, { }, RestructureType.Remove(index)))
    }

    /**
     * Removes all declared [AdapterViewType]. Use only in combination with other operators, do not leave adapter empty,
     * otherwise [ZeroViewTypes] can be thrown. todo: review ZeroViewTypes exception, maybe is useless
     *
     * @throws ZeroViewTypes when adapter holds zero view types
     */
    @ConfigurationDsl
    fun removeAll() {
        restructureQueue.add(RestructureItem(null, { }, RestructureType.RemoveAll))
    }

    /**
     * Replace [AdapterViewType] by tag
     * @param tag Tag which was associated to [AdapterViewType] when it was declared. This tag will be associated with
     * new [AdapterViewType]
     * @param block configuration of a new [AdapterViewType]
     *
     * @throws UndeclaredTag in case of invalid tag
     */
    @ConfigurationDsl
    fun replace(tag: String, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.ReplaceByTag(tag)))
    }

    /**
     * Replace [AdapterViewType] by index
     * @param index [AdapterViewType] position where it must been replaced
     * @param newTag nullable tag. New [AdapterViewType] can be marked with a tag for future comfortable manipulation.
     * @param block configuration of a new [AdapterViewType]
     *
     * @throws IndexOutOfBoundsException in case of invalid index
     */
    @ConfigurationDsl
    fun replace(index: Int, newTag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(newTag, block, RestructureType.ReplaceByIndex(index)))
    }

    internal fun doUpdate(typedKidAdapterConfiguration: TypedKidAdapterConfiguration) {
        val viewTypes = typedKidAdapterConfiguration.viewTypes
        restructureQueue.forEach {
            when (it.restructureType) {
                is RestructureType.Insert.InsertTop -> viewTypes.add(0, AdapterViewType(it.tag, 0, it.configuration))
                is RestructureType.Insert.InsertBottom -> viewTypes.add(AdapterViewType(it.tag, 0, it.configuration))
                is RestructureType.Insert -> viewTypes.add(
                    it.restructureType.index,
                    AdapterViewType(it.tag, 0, it.configuration)
                )
                is RestructureType.Remove -> {
                    if (it.tag != null) {
                        viewTypes.remove(viewTypes.first { item -> item.tag == it.tag })
                    }
                    if (it.restructureType.index != -1) {
                        viewTypes.removeAt(it.restructureType.index)
                    }
                }
                is RestructureType.RemoveAll -> viewTypes.clear()
                is RestructureType.ReplaceByIndex -> viewTypes[it.restructureType.index] = AdapterViewType(it.tag, 0, it.configuration)
                is RestructureType.ReplaceByTag -> {
                    val index = viewTypes.indexOfFirst { item -> item.tag == it.tag }
                    viewTypes[index] = AdapterViewType(it.tag, 0, it.configuration)
                }
            }
        }
        typedKidAdapterConfiguration.invalidateItems()
    }
}