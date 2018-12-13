package com.link184.kidadapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.simple.SingleKidAdapter
import com.link184.kidadapter.simple.SingleKidAdapterConfiguration
import com.link184.kidadapter.typed.TypedKidAdapter
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

fun <T> RecyclerView.setUp(block: SingleKidAdapterConfiguration<T>.() -> Unit): SingleKidAdapter<T> {
    val configuration = SingleKidAdapterConfiguration<T>().apply(block)
    return SingleKidAdapter(configuration).apply {
        layoutManager = configuration.layoutManager ?: LinearLayoutManager(context)
        adapter = this
    }
}

fun RecyclerView.setUp(block: TypedKidAdapterConfiguration.() -> Unit): TypedKidAdapter {
    val adapterDsl = TypedKidAdapterConfiguration().apply(block)
    return TypedKidAdapter(adapterDsl).apply {
        layoutManager = adapterDsl.layoutManager ?: LinearLayoutManager(context)
        adapter = this
    }
}