package com.link184.sample

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_single_type.*
import kotlinx.android.synthetic.main.item_text.view.*
import java.util.*

class SingleTypeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)

        recyclerView.setUp<String> {
            withLayoutResId(R.layout.item_text)
            withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
            bind {
                (stringName.parent as ViewGroup).setBackgroundColor(getRandomColor())
                stringName.text = it
            }
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random(System.nanoTime())
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}