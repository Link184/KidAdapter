package com.link184.kidadapter.typed

data class UpdateItem<T>(val modelType: Class<T>?,
                         val tag: String?,
                         val items: MutableList<T>) {
    constructor(modelType: Class<T>, tag: String?, item: T) : this(modelType, tag, mutableListOf(item))
}