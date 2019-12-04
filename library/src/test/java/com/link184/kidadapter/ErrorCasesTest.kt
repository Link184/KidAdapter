package com.link184.kidadapter

/* ktlint-disable no-wildcard-imports */
import android.os.Build
import android.widget.TextView
import com.link184.kidadapter.exceptions.*
/* ktlint-enable no-wildcard-imports */
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
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
    fun t2_withLayoutResAndViewDefined() {
        assertFailsWith<UndefinedLayout> {
            activityController.get().recyclerView.setUp<String> {
                withItems(mutableListOf("One", "Two"))
                withLayoutResId(android.R.layout.list_content)
                withLayoutView(::TextView)
                bind { }
            }
        }

        assertFailsWith<UndefinedLayout> {
            activityController.get().recyclerView.setUp {
                withViewType {
                    withItems(mutableListOf("one", "two"))
                    withLayoutResId(android.R.layout.list_content)
                    withLayoutView(::TextView)
                    bind<String> { }
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
            adapter update {
                insertTop(1_000_000_000)
            }
        }

        assertFailsWith<UndeclaredTag> {
            adapter update {
                insertBottom(333_333, "UNDEFINED TAG")
            }
        }
    }

    @Test
    fun t4_wrongTag() {
        val adapter = activityController.get().recyclerView.setUp {
            withViewType("first_tag") {
                withItems(mutableListOf("one", "two"))
                withLayoutResId(android.R.layout.list_content)
                bind<String> { }
            }

            withViewType("second_tag") {
                withItems(mutableListOf(1, 2, 3))
                withLayoutResId(android.R.layout.list_content)
                bind<Int> { }
            }
        }

        assertFailsWith<WrongTagType> {
            adapter update {
                insertTop(mutableListOf(4, 5, 6), "first_tag")
            }
        }
    }
}