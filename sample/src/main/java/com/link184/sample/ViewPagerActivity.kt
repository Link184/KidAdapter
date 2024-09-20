package com.link184.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.link184.kidadapter.setUp

class ViewPagerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        findViewById<ViewPager2>(R.id.viewPager).setUp<String> {
            withItems(mutableListOf("one", "two", "three"))
            withLayoutResId(R.layout.view_tab)
            bind { findViewById<TextView>(R.id.name).text = it }
        }
    }
}