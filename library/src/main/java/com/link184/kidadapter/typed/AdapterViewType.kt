package com.link184.kidadapter.typed

class AdapterViewType<T>(fromPosition: Int, block: AdapterViewTypeConfiguration.() -> Unit) {
    internal val configuration = AdapterViewTypeConfiguration().apply(block)
    internal var positionRange: IntRange = fromPosition until (fromPosition + configuration.getInternalItems().size)
    val viewType: Int = this.hashCode()
}