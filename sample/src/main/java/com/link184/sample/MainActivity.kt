package com.link184.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        multiTypeButton.setOnClickListener { startActivity(Intent(this, MultiTypeActivity::class.java)) }
        singleTypeButton.setOnClickListener { startActivity(Intent(this, SingleTypeActivity::class.java)) }
        multiTypeWithTagsButton.setOnClickListener { startActivity(Intent(this, MultiTypeWithTagsActivity::class.java)) }
        viewPager2Adapter.setOnClickListener { startActivity(Intent(this, ViewPagerActivity::class.java)) }
    }
}
