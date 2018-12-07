package com.link184.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
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
                bind<String> {
                    stringName.text = it
                }
            }

            withViewType {
                withLayoutResId(R.layout.item_int)
                withItems(mutableListOf(1, 2, 3, 4, 5, 6))
                bind<Int> {
                    intName.text = it.toString()
                }
            }


            withViewType(SECOND_STRING_TAG) {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("eight", "nine", "ten", "eleven", "twelve"))
                bind<String> {
                    stringName.text = it
                }
            }
        }

        recyclerView.postDelayed({
            adapter.update {
                insertBottom(mutableListOf("thirteen", "fourteen"), SECOND_STRING_TAG)
            }
        }, 2_000)

        recyclerView.postDelayed({
            adapter.update {
                removeItems(mutableListOf(2, 4, 5))
            }
        }, 4_000)
    }
}