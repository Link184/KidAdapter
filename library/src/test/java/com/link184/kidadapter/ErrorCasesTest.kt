package com.link184.kidadapter

import com.link184.kidadapter.exceptions.UndeclaredTag
import com.link184.kidadapter.exceptions.UndeclaredTypeModification
import com.link184.kidadapter.exceptions.UndefinedLayout
import com.link184.kidadapter.exceptions.ZeroViewTypes
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ErrorCasesTest {
    lateinit var activityController: ActivityController<RecyclerViewActivity>
    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(RecyclerViewActivity::class.java)
    }

    @Test
    fun t1_withoutLayoutResource() {
        assertFailsWith<UndefinedLayout> {
            activityController.get().recyclerView.setUp<String> {
                withItems(mutableListOf("one", "two"))
                bind {
                }
            }
        }

        assertFailsWith<UndefinedLayout> {
            activityController.get().recyclerView.setUp {
                withViewType {
                    withItems(mutableListOf("one", "two", "three"))
                    bind<String> {
                    }
                }
            }
        }
    }

    @Test
    fun t2_zeroViewTypes() {
        assertFailsWith<ZeroViewTypes> {
            activityController.get().recyclerView.setUp { }
        }
    }

    @Test
    fun t3_undeclaredTypeUpdate() {
        val adapter = activityController.get().recyclerView.setUp {
            withViewType {
                withItems(mutableListOf("one", "two"))
                withLayoutResId(android.R.layout.list_content)
                bind<String> { }
            }
        }

        assertFailsWith<UndeclaredTypeModification> {
            adapter.update {
                insertTop(1_000_000_000)
            }
        }

        assertFailsWith<UndeclaredTag> {
            adapter.update {
                insertBottom(333_333, "UNDEFINED TAG")
            }
        }
    }
}