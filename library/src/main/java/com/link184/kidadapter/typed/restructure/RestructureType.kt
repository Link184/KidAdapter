package com.link184.kidadapter.typed.restructure

sealed class RestructureType {
    sealed class Insert(internal val index: Int) : RestructureType() {
        object InsertTop : Insert(0)
        class InsertMiddle(index: Int) : Insert(index)
        object InsertBottom : Insert(-1)
    }

    object ReplaceAll : RestructureType()
    class ReplaceByIndex(internal val index: Int) : RestructureType()
    class ReplaceByTag(internal val tag: String) : RestructureType()

    class Remove(internal val index: Int) : RestructureType()
    object RemoveAll : RestructureType()

    fun resolveIndex(currentList: MutableList<*>): Int {
        if (this is Insert.InsertBottom) {
            return currentList.size
        }
        if (this is Insert) {
            return index
        }
        if (this is Remove) {
            return index
        }
        if (this is ReplaceByIndex) {
            return index
        }
        return -1
    }
}