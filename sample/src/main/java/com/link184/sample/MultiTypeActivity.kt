package com.link184.sample

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.link184.kidadapter.setUp

class MultiTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type)

        with(findViewById<RecyclerView>(R.id.recyclerView)) {
            addItemDecoration(
                DividerItemDecoration(
                    this@MultiTypeActivity,
                    DividerItemDecoration.VERTICAL
                )
            )

            setUp {
                withViewType {
                    withLayoutResId(R.layout.item_text)
                    withItems(
                        mutableListOf(
                            "one",
                            "two",
                            "three",
                            "four",
                            "five",
                            "six",
                            "seven"
                        )
                    )
                    bindIndexed<String> { item, position ->
                        findViewById<TextView>(R.id.stringName).text = item
                        setOnClickListener {
                            Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                withViewType {
                    withLayoutResId(R.layout.item_int)
                    withItems(mutableListOf(8, 9, 10, 11, 12, 13))
                    bindIndexed<Int> { item, position ->
                        findViewById<TextView>(R.id.intName).text = item.toString()
                        setOnClickListener {
                            Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}