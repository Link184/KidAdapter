package com.link184.kidadapter

import android.os.Build
import com.link184.kidadapter.simple.SingleKidAdapterConfiguration
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.internal.verification.VerificationModeFactory.times
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SingleKidAdapterTest {
    lateinit var activityController: ActivityController<RecyclerViewActivity>
    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(RecyclerViewActivity::class.java)
    }

    @Test
    fun t1_simpleAdapterPopulateTest() {
        val items = mutableListOf("a", "b", "c", "d")
        val bindFunction: SingleKidAdapterConfiguration<String>.(String) -> Unit = { println(it) }
        val bindFunctionIndexed: SingleKidAdapterConfiguration<String>.(String, Int) -> Unit = { item, index ->
            println("$item $index")
        }
        val bindFunctionSpy = spy(bindFunction)
        val bindIndexedFunctionSpy = spy(bindFunctionIndexed)

        activityController.get().recyclerView.setUp<String> {
            withItems(items)
            withLayoutResId(android.R.layout.list_content)
            bindIndexed { item, index ->
                assertTrue(items.contains(item))
                bindIndexedFunctionSpy(item, index)
            }
            bind {
                assertTrue(items.contains(it))
                bindFunctionSpy(it)
            }
        }

        activityController.create().start().visible()
        Mockito.verify(bindFunctionSpy, times(items.size)).invoke(anyNullableObject(), anyNullableObject())
        Mockito.verify(bindIndexedFunctionSpy, times(items.size))
            .invoke(anyNullableObject(), anyNullableObject(), anyInt())
    }

    @Test
    fun t2_operationsTest() {
        val items = mutableListOf(1, 2, 3, 4, 5)
        val initialItemsSize = items.size

        val adapter = activityController.get().recyclerView.setUp<Int> {
            withItems(items)
            withLayoutResId(android.R.layout.list_content)
            bindIndexed { item, index ->
                assertTrue(items.contains(item))
            }
            bind {
                assertTrue(items.contains(it))
            }
        }

        adapter + 6 + 7 + 8
        assertEquals(items.size, initialItemsSize + 3)

        adapter + 9 - 4 - 1
        assertEquals(items.size, initialItemsSize + 3 + 1 - 2)
        assertTrue {
            !items.contains(1)
            !items.contains(4)
        }
    }

    @Test
    fun t3_updateAdapterTest() {
        val items = mutableListOf(1, 2, 3, 4, 5)
        val initialItemsSize = items.size

        val bindFunction: SingleKidAdapterConfiguration<Int>.(Int) -> Unit = { println(it) }
        val bindFunctionSpy = spy(bindFunction)

        val adapter = activityController.get().recyclerView.setUp<Int> {
            withItems(items)
            withLayoutResId(android.R.layout.list_content)
            bindIndexed { item, index ->
                assertTrue(items.contains(item))
            }
            bind {
                assertTrue(items.contains(it))
                bindFunctionSpy(it)
            }
        }

        activityController.create().start().visible()

        Mockito.verify(bindFunctionSpy, times(initialItemsSize)).invoke(anyNullableObject(), anyInt())

        adapter update {
            it.add(6)
            it.add(7)
            it.remove(2)
            it[0] = 1
        }

        activityController.create().start().visible()

        Mockito.verify(bindFunctionSpy, times(initialItemsSize + 3)).invoke(anyNullableObject(), anyInt())
        assertNotEquals(initialItemsSize, adapter.itemCount)
        assertTrue { adapter.getAllItems().indexOf(3) == 1 }
    }
}