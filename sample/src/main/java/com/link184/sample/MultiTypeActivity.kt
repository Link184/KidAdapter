package com.link184.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.link184.kidadapter.setUp
import kotlinx.android.synthetic.main.activity_multi_type.*
import kotlinx.android.synthetic.main.item_int.view.*
import kotlinx.android.synthetic.main.item_text.view.*

class MultiTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.setUp {
            withViewType {
                withLayoutResId(R.layout.item_text)
                withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
                bindIndexed<String> { item, position ->
                    stringName.text = item
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            withViewType {
                withLayoutResId(R.layout.item_int)
                withItems(mutableListOf(8, 9, 10, 11, 12, 13))
                bindIndexed<Int> { item, position ->
                    intName.text = item.toString()
                    setOnClickListener {
                        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}