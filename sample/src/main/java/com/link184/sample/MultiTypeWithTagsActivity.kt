package com.link184.sample

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.setUp

private const val FIRST_STRING_TAG = "first_string_tag"
private const val SECOND_STRING_TAG = "second_string_tag"

class MultiTypeWithTagsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type_with_tags)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val adapter = recyclerView.setUp {
            withViewType(FIRST_STRING_TAG) {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
                bindIndexed<String> { item, position ->
                    findViewById<TextView>(R.id.stringName).text = item
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            withViewType {
                withLayoutResId(R.layout.item_int)
                withItems(mutableListOf(1, 2, 3, 4, 5, 6))
                bindIndexed<Int> { item, position ->
                    findViewById<TextView>(R.id.intName).text = item.toString()
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            withViewType(SECOND_STRING_TAG) {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("eight", "nine", "ten", "eleven", "twelve"))
                bindIndexed<String> { item, position ->
                    findViewById<TextView>(R.id.stringName).text = item
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        recyclerView.postDelayed({
            adapter update {
                insertBottom(mutableListOf("thirteen", "fourteen"), SECOND_STRING_TAG)
            }
        }, 2_000)

        recyclerView.postDelayed({
            adapter update {
                removeItems(mutableListOf(2, 4, 5))
            }
        }, 4_000)

        recyclerView.postDelayed({
            adapter update {
                insert(2, mutableListOf("New String 1", "New String 2"), FIRST_STRING_TAG)
            }
        }, 6_000)

        recyclerView.postDelayed({
            adapter update {
                swap<String>(2, 4, FIRST_STRING_TAG)
            }
        }, 8_000)

        recyclerView.postDelayed({
            adapter update {
                removeAll<String>()
                removeAll<Int>()
                removeAll<String>(SECOND_STRING_TAG)
            }
        }, 14_000)
    }
}