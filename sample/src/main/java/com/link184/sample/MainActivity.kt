package com.link184.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.multiTypeButton).setOnClickListener { startActivity(Intent(this, MultiTypeActivity::class.java)) }
        findViewById<View>(R.id.singleTypeButton).setOnClickListener { startActivity(Intent(this, SingleTypeActivity::class.java)) }
        findViewById<View>(R.id.multiTypeWithTagsButton).setOnClickListener { startActivity(Intent(this, MultiTypeWithTagsActivity::class.java)) }
        findViewById<View>(R.id.viewPager2Adapter).setOnClickListener { startActivity(Intent(this, ViewPagerActivity::class.java)) }
    }
}
