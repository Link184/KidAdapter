package com.link184.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_single_type.*
import kotlinx.android.synthetic.main.item_text.view.*
import java.util.*

class SingleTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)


        val adapter = recyclerView.setUp<String> {
            withLayoutResId(R.layout.item_text)
            withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
            bindIndexed { item, position ->
                setBackgroundColor(getRandomColor())
                stringName.text = item
                setOnClickListener {
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }


        recyclerView.postDelayed({ adapter + "1" }, 2_000)
        recyclerView.postDelayed({ adapter + mutableListOf("2", "3", "4") }, 4_000)
        recyclerView.postDelayed({ adapter[2] = "3" }, 4_000)
        recyclerView.postDelayed({ adapter.clear() }, 8_000)
    }

    private fun getRandomColor(): Int {
        val rnd = Random(System.nanoTime())
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }


}
