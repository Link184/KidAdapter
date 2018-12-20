package com.link184.kidadapter

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView

class RecyclerViewActivity : Activity() {
    val recyclerView by lazy {
        RecyclerView(this).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(recyclerView)
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 10000)
    }
}