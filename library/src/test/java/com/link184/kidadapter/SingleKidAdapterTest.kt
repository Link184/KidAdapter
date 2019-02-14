package com.link184.kidadapter

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
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
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
        Mockito.verify(bindIndexedFunctionSpy, times(items.size)).invoke(anyNullableObject(), anyNullableObject(), anyInt())
    }
}