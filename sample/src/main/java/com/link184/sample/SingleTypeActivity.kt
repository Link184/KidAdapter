package com.link184.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.setUp
import java.util.Random

class SingleTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = recyclerView.setUp<String> {
            withLayoutResId(R.layout.item_text)
            withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
            bindIndexed { item, position ->
                setBackgroundColor(getRandomColor())
                findViewById<TextView>(R.id.stringName).text = item
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
