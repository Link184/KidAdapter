package com.link184.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_view_pager.*
import kotlinx.android.synthetic.main.view_tab.view.*

class ViewPagerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        viewPager.setUp<String> {
            withItems(mutableListOf("one", "two", "three"))
            withLayoutResId(R.layout.view_tab)
            bind { name.text = it }
        }
    }
}