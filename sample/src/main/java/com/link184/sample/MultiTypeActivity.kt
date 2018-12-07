package com.link184.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_multi_type.*
import kotlinx.android.synthetic.main.item_int.view.*
import kotlinx.android.synthetic.main.item_text.view.*

class MultiTypeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type)

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setUp {
            withViewType {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
                bind<String> {
                    stringName.text = it
                }
            }

            withViewType {
                withLayoutResId(R.layout.item_int)
                withItems(mutableListOf(8, 9, 10, 11, 12, 13))
                bind<Int> {
                    intName.text = it.toString()
                }
            }
        }

    }
}