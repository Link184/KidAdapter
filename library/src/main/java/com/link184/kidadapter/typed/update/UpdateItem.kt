package com.link184.kidadapter.typed.update

data class UpdateItem<T>(
    val modelType: Class<T>?,
    val tag: String?,
    val items: List<T>,
    val updateType: UpdateType
) {
    constructor(modelType: Class<T>, tag: String?, item: T, updateType: UpdateType)
            : this(modelType, tag, mutableListOf(item), updateType)
}