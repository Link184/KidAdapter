package com.link184.kidadapter.typed.restructure

import com.link184.kidadapter.ConfigurationDsl
import com.link184.kidadapter.exceptions.ZeroViewTypes
import com.link184.kidadapter.typed.AdapterViewType
import com.link184.kidadapter.typed.AdapterViewTypeConfiguration
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

class RestructureConfiguration {
    private val restructureQueue = mutableListOf<RestructureItem>()

    @ConfigurationDsl
    fun insert(index: Int, tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.Insert.InsertMiddle(index)))
    }

    @ConfigurationDsl
    fun insertTop(tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.Insert.InsertTop))
    }

    @ConfigurationDsl
    fun insertBottom(tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.Insert.InsertBottom))
    }

    @ConfigurationDsl
    fun remove(tag: String? = null) {
        restructureQueue.add(RestructureItem(tag, { }, RestructureType.Remove(-1)))
    }

    @ConfigurationDsl
    fun remove(index: Int) {
        restructureQueue.add(RestructureItem(null, { }, RestructureType.Remove(index)))
    }

    /**
     * Removes all declared [AdapterViewType]. Use only in combination with other operators, do not leave adapter empty,
     * otherwise [ZeroViewTypes] can be thrown. todo: review ZeroViewTypes exception, maybe is useless
     */
    @ConfigurationDsl
    fun removeAll() {
        restructureQueue.add(RestructureItem(null, { }, RestructureType.RemoveAll))
    }

    @ConfigurationDsl
    fun replace(tag: String, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.ReplaceByTag(tag)))
    }

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