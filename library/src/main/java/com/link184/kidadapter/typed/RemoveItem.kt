package com.link184.kidadapter.typed

data class RemoveItem<T>(val modelType: Class<T>? = null,
                      val tag: String? = null,
                      val items: MutableList<T> = mutableListOf()) {
    fun toUpdateItem() = UpdateItem(modelType, tag, items)
}