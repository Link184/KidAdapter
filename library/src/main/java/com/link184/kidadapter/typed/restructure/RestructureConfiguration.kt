package com.link184.kidadapter.typed.restructure

import com.link184.kidadapter.ConfigurationDsl
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

    @ConfigurationDsl
    fun removeAll() {
        restructureQueue.add(RestructureItem(null, { }, RestructureType.RemoveAll))
    }

    @ConfigurationDsl
    fun replace(tag: String?, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(tag, block, RestructureType.Replace(-1)))
    }

    @ConfigurationDsl
    fun replace(index: Int, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(null, block, RestructureType.Replace(index)))
    }

    internal fun doUpdate(typedKidAdapterConfiguration: TypedKidAdapterConfiguration) {
        val viewTypes = typedKidAdapterConfiguration.viewTypes
        restructureQueue.forEach {
            when (it.restructureType) {
                is RestructureType.Insert -> viewTypes.add(
                    it.restructureType.index,
                    AdapterViewType(0, it.configuration)
                )
                is RestructureType.Insert.InsertTop -> viewTypes.add(0, AdapterViewType(0, it.configuration))
                is RestructureType.Insert.InsertBottom -> viewTypes.add(viewTypes.lastIndex, AdapterViewType(0, it.configuration))
                is RestructureType.Remove -> {
                    if (it.tag != null) {
                        //todo: do more better
                        typedKidAdapterConfiguration.ta
                    }
                    if (it.restructureType.index != -1) {
                        viewTypes.removeAt(it.restructureType.index)
                    }
                }
                is RestructureType.RemoveAll -> viewTypes.clear()
                is RestructureType.Replace -> {
                    if (it.tag != null) {
                        //todo: do more better
                        typedKidAdapterConfiguration.ta
                    }
                    if (it.restructureType.index != -1) {
                        viewTypes.set(it.restructureType.index, AdapterViewType(0, it.configuration))
                    }
                }
            }
        }
        typedKidAdapterConfiguration.invalidateItems()
    }
}