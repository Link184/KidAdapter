package com.link184.sample

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_single_type.*
import java.util.*

class SingleTypeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)

        val adapter = recyclerView.setUp<String> {
            withLayoutResId(R.layout.item_text)
            withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
            bind {
                (stringName.parent as ViewGroup).setBackgroundColor(getRandomColor())
                stringName.text = it
            }
        }

        recyclerView.postDelayed({ adapter + "1" }, 2_000)
    }

    private fun getRandomColor(): Int {
        val rnd = Random(System.nanoTime())
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}