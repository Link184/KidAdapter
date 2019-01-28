package com.link184.kidadapter.typed.update

sealed class UpdateType {
    sealed class Insert(internal val index: Int) : UpdateType() {
        object InsertTop : Insert(0)
        class InsertMiddle(index: Int) : Insert(index)
        object InsertBottom : Insert(-1)
    }

    object ReplaceAll : UpdateType()
    object Remove : UpdateType()

    class Swap(val firstIndex: Int, val secondIndex: Int) : UpdateType()

    fun resolveIndex(currentList: MutableList<*>): Int {
        if (this is Insert.InsertBottom) {
            return currentList.size
        }
        if (this is Insert) {
            return index
        }
        return -1
    }
}