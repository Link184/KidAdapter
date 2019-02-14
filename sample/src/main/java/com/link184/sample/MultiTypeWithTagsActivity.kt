package com.link184.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_multi_type_with_tags.*
import kotlinx.android.synthetic.main.item_int.view.*
import kotlinx.android.synthetic.main.item_text.view.*

private const val FIRST_STRING_TAG = "first_string_tag"
private const val SECOND_STRING_TAG = "second_string_tag"

class MultiTypeWithTagsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type_with_tags)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val adapter = recyclerView.setUp {
            withViewType(FIRST_STRING_TAG) {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
                bind<String> { item, position ->
                    stringName.text = item
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            withViewType {
                withLayoutResId(R.layout.item_int)
                withItems(mutableListOf(1, 2, 3, 4, 5, 6))
                bind<Int> { item, position ->
                    intName.text = item.toString()
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            withViewType(SECOND_STRING_TAG) {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("eight", "nine", "ten", "eleven", "twelve"))
                bind<String> { item, position ->
                    stringName.text = item
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