package com.link184.kidadapter

import com.link184.kidadapter.typed.TypedKidAdapter
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestructureTest {
    private lateinit var activityController: ActivityController<RecyclerViewActivity>
    private lateinit var adapter: TypedKidAdapter
    private var oldListSize: Int = 0

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(RecyclerViewActivity::class.java)
        adapter = setUpRecyclerView()
        oldListSize = adapter.itemCount
    }

    private fun setUpRecyclerView(): TypedKidAdapter {
        return activityController.get().recyclerView.setUp {
            withViewType {
                withItems(mutableListOf(1, 2, 3))
                withLayoutResId(android.R.layout.list_content)
                bind<Int> { }
            }

            withViewType {
                withItems(mutableListOf(1.1, 2.2, 3.3))
                withLayoutResId(android.R.layout.list_content)
                bind<Float> { }
            }

            withViewType {
                withItems(mutableListOf("one", "two", "three"))
                withLayoutResId(android.R.layout.list_content)
                bind<String> { }
            }
        }
    }

    @Test
    fun t1_insertViewType() {
        val items = mutableListOf(4, 5, 6, 7)
        adapter restructure {
            insert(tag = "tag") {
                withItems(items)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        activityController.create().start().visible()

        assertTrue { adapter.itemCount != oldListSize }
        assertTrue { adapter.itemCount > oldListSize }
        assertTrue { adapter.itemCount == items.size + oldListSize }
    }
}