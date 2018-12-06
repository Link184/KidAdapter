package com.link184.kidadapter.typed

sealed class UpdateType(private val index: Int) {
    class InsertTop: UpdateType(0)
    class InsertMiddle(index: Int) : UpdateType(index)
    class InsertBottom : UpdateType(-1)

    fun resolveIndex(currentList: MutableList<Any>): Int {
        if (this is InsertBottom) {
            return currentList.lastIndex
        }
        return index
    }
}