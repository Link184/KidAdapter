package com.link184.kidadapter.typed

class AdapterViewType<T>(fromPosition: Int, block: AdapterViewTypeConfiguration.() -> Unit) {
    internal val configuration = AdapterViewTypeConfiguration().apply(block)
    internal var positionRange: IntRange = fromPosition until (fromPosition + configuration.getInternalItems().size)
    internal var tag: String? = null
    val viewType: Int = this.hashCode()

    internal constructor(tag: String? = null, fromPosition: Int, block: AdapterViewTypeConfiguration.() -> Unit): this(fromPosition, block) {
        this.tag = tag
    }
}