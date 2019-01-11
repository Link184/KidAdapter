package com.link184.kidadapter.typed.restructure

import com.link184.kidadapter.typed.AdapterViewType
import com.link184.kidadapter.typed.AdapterViewTypeConfiguration
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

class RestructureConfiguration {
    private val restructureQueue = mutableListOf<RestructureItem>()

    fun insert(index: Int? = null, tag: String? = null, block: AdapterViewTypeConfiguration.() -> Unit) {
        restructureQueue.add(RestructureItem(index, tag, block))
    }

    internal fun doUpdate(typedKidAdapterConfiguration: TypedKidAdapterConfiguration) {
        restructureQueue.forEach {

        }
    }

    private fun insert(item: RestructureItem, typedKidAdapterConfiguration: TypedKidAdapterConfiguration) {
        typedKidAdapterConfiguration.viewTypes
            .add(item.index ?: typedKidAdapterConfiguration.viewTypes.lastIndex, AdapterViewType())
    }
}