package com.link184.kidadapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.link184.kidadapter.simple.SingleKidAdapter
import com.link184.kidadapter.simple.SingleKidAdapterConfiguration
import com.link184.kidadapter.typed.TypedKidAdapter
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration

/**
 * Simple way to create [RecyclerView.Adapter] with no view types
 * @param block hare adapter can be configured
 * @param T adapter item models type.
 * @receiver any instance of [RecyclerView]
 * @return a child instance of a [RecyclerView.Adapter]
 */
fun <T> RecyclerView.setUp(block: SingleKidAdapterConfiguration<T>.() -> Unit): SingleKidAdapter<T> {
    val configuration = SingleKidAdapterConfiguration<T>().apply(block)
    return SingleKidAdapter(configuration).apply {
        if (layoutManager == null) {
            layoutManager = configuration.layoutManager ?: LinearLayoutManager(context)
        }
        adapter = this
    }
}

/**
 * Simple way to create [RecyclerView.Adapter] with view types.
 * @param block hare adapter can be configured
 * @receiver any instance of [RecyclerView]
 * @return a child instance of a [RecyclerView.Adapter]
 */
fun RecyclerView.setUp(block: TypedKidAdapterConfiguration.() -> Unit): TypedKidAdapter {
    val adapterDsl = TypedKidAdapterConfiguration().apply(block)
    return TypedKidAdapter(adapterDsl).apply {
        if (layoutManager == null) {
            layoutManager = adapterDsl.layoutManager ?: LinearLayoutManager(context)
        }
        adapter = this
    }
}

/**
 * Simple way to create [RecyclerView.Adapter] with no view types
 * @param block hare adapter can be configured
 * @param T adapter item models type.
 * @receiver any instance of [ViewPager2]
 * @return a child instance of a [RecyclerView.Adapter]
 */
fun <T> ViewPager2.setUp(block: SingleKidAdapterConfiguration<T>.() -> Unit): SingleKidAdapter<T> {
    val configuration = SingleKidAdapterConfiguration<T>().apply(block)
    return SingleKidAdapter(configuration).apply {
        adapter = this
    }
}