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

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestructureTest {
    private lateinit var activityController: ActivityController<RecyclerViewActivity>
    private lateinit var adapter: TypedKidAdapter

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(RecyclerViewActivity::class.java)
        adapter = setUpRecyclerView()
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
                bind<Int> { }
            }
        }
    }

    @Test
    fun t1_insertViewType() {
        activityController.create().start().visible()

        adapter.restructure {
            insert(tag = "tag") {
                withItems(mutableListOf(4, 5, 6, 7))
                withLayoutResId(android.R.layout.list_content)
                bind<Int> { }
            }
        }
    }
}