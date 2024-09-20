package com.link184.kidadapter

import android.R
import android.os.Build
import com.link184.kidadapter.exceptions.UndeclaredTag
import com.link184.kidadapter.typed.AdapterViewTypeConfiguration
import com.link184.kidadapter.typed.TypedKidAdapter
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
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
    fun t01_insertViewType() {
        val items = mutableListOf(4, 5, 6, 7)
        adapter restructure {
            insert(2, "tag") {
                withItems(items)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        activityController.create().start().visible()

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == items.size + oldListSize
        }
    }

    @Test
    fun t02_insertTopViewType() {
        val items = mutableListOf("2", "3", "4")
        adapter restructure {
            insertTop {
                withItems(items)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == items.size + oldListSize
        }

        assertTrue {
            adapter[0] == "2" &&
                    adapter[1] == "3" &&
                    adapter[2] == "4" &&
                    adapter[3] == 1
        }
    }

    @Test
    fun t03_insertBottomViewType() {
        val items = mutableListOf(5.5, 6.6, 7.7)
        adapter restructure {
            insertBottom {
                withItems(items)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == items.size + oldListSize
        }

        assertTrue {
            adapter[adapter.itemCount - 1] == 7.7 &&
                    adapter[adapter.itemCount - 2] == 6.6 &&
                    adapter[adapter.itemCount - 3] == 5.5
        }
    }

    @Test
    fun t04_removeByTagViewType() {
        adapter restructure {
            insert(2, "myTag") {
                withItem("22")
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        val newItem = adapter.getItemsByType<String>("myTag").first()
        assertNotNull(newItem)
        assertEquals("22", newItem)

        adapter restructure {
            remove("myTag")
        }

        assertFailsWith<UndeclaredTag> {
            adapter.getItemsByType<String>("myTag").first()
        }

        assertTrue {
            adapter.itemCount == oldListSize
        }
    }

    @Test
    fun t05_removeByIndexViewType() {
        adapter restructure {
            insert(2, "myTag") {
                withItem("22")
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        val newItem = adapter.getItemsByType<String>("myTag").first()
        assertNotNull(newItem)
        assertEquals("22", newItem)

        adapter restructure {
            remove(2)
        }

        assertFailsWith<UndeclaredTag> {
            adapter.getItemsByType<String>("myTag").first()
        }

        assertTrue {
            adapter.itemCount == oldListSize
        }
    }

    @Test
    fun t06_removeAllViewType() {
        adapter.restructure {
            removeAll()
        }
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun t07_replaceByTagViewType() {
        adapter restructure {
            insertBottom("myTag") {
                withItem("22")
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        val newItem = adapter.getItemsByType<String>("myTag").first()
        assertNotNull(newItem)
        assertEquals("22", newItem)

        adapter restructure {
            replace("myTag") {
                withItem(44)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        val replacedItem = adapter.getItemsByType<Int>("myTag").first()
        assertNotNull(replacedItem)
        assertEquals(44, replacedItem)
    }

    @Test
    fun t08_replaceByIndexViewType() {
        adapter restructure {
            insert(2, "myTag") {
                withItem("22")
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        val newItem = adapter.getItemsByType<String>("myTag").first()
        assertNotNull(newItem)
        assertEquals("22", newItem)

        adapter restructure {
            replace(2, "myNewTag") {
                withItem(44)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        assertFailsWith<UndeclaredTag> {
            adapter.getItemsByType<Int>("myTag")
        }
        val replacedItem = adapter.getItemsByType<Int>("myNewTag").first()
        assertNotNull(replacedItem)
        assertEquals(44, replacedItem)

        adapter restructure {
            replace(2) {
                withItem(500)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    println("We are here $it")
                }
            }
        }

        assertTrue {
            adapter.itemCount != oldListSize &&
                    adapter.itemCount > oldListSize &&
                    adapter.itemCount == 1 + oldListSize
        }

        assertFailsWith<UndeclaredTag> { adapter.getItemsByType<Int>("myTag") }
        assertFailsWith<UndeclaredTag> { adapter.getItemsByType<Int>("myNewTag") }

        val veryReplacedItem: Int = adapter[6] as Int
        assertEquals(500, veryReplacedItem)
    }

    @Test
    fun t09_swapViewType() {
        adapter restructure {
            swap(0, 2)
        }

        assertTrue {
            oldListSize == adapter.itemCount &&
                    adapter[0] is String &&
                    adapter[adapter.itemCount - 1] is Int
        }
    }

    @Test
    fun t10_mixedRestructuringViewType() {
        val simpleViewType: AdapterViewTypeConfiguration.() -> Unit = {
            withItem(44)
            withLayoutResId(R.layout.list_content)
            bind<Int> {
                println("We are here $it")
            }
        }
        adapter restructure {
            insertTop("top1", simpleViewType)
            insert(3, "insertAt3", simpleViewType)
            insertBottom("bottom1", simpleViewType)
            replace("top1", simpleViewType)
        }

        assertTrue {
            oldListSize < adapter.itemCount &&
                    oldListSize + 3 == adapter.itemCount
        }
        assertEquals(44, adapter[adapter.itemCount - 1])

        adapter restructure {
            insertBottom("bottom1", simpleViewType)
            insertBottom("bottom2", simpleViewType)
            insertBottom("bottom3", simpleViewType)
            insertBottom("bottom4", simpleViewType)
            insertBottom("bottom5", simpleViewType)
            removeAll()
            insertTop("top1", simpleViewType)
        }

        assertTrue { adapter.itemCount == 1 }
        assertEquals(44, adapter[0])
    }
}