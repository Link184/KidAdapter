package com.link184.kidadapter

import android.os.Build
import com.link184.kidadapter.typed.TypedKidAdapter
import com.link184.kidadapter.typed.TypedKidAdapterConfiguration
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.internal.verification.VerificationModeFactory
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MultiAdapterTest {
    private lateinit var activityController: ActivityController<RecyclerViewActivity>

    private val stringItems = mutableListOf("a", "b", "c", "d")
    private val stringBindFunction: TypedKidAdapterConfiguration.(String) -> Unit = { }
    private var stringBindFunctionSpy = spy(stringBindFunction)
    private val intItems = mutableListOf(1, 2, 3, 4, 5, 9)
    private val intBindFunction: TypedKidAdapterConfiguration.(Int) -> Unit = { }
    private var intBindFunctionSpy = spy(intBindFunction)
    private val anyItems = mutableListOf(Any(), Any(), Any(), Any())
    private val anyBindFunction: TypedKidAdapterConfiguration.(Any) -> Unit = { }
    private var anyBindFunctionSpy = spy(anyBindFunction)

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(RecyclerViewActivity::class.java)
        resetSpys()
    }

    private fun resetSpys() {
        stringBindFunctionSpy = spy(stringBindFunction)
        intBindFunctionSpy = spy(intBindFunction)
        anyBindFunctionSpy = spy(anyBindFunction)
    }

    private fun populateRecyclerView(): TypedKidAdapter {
        return activityController.get().recyclerView.setUp {
            withViewType {
                withItems(stringItems)
                withLayoutResId(android.R.layout.list_content)
                bind<String> {
                    assertTrue(stringItems.contains(it))
                    stringBindFunctionSpy(it)
                }
            }

            withViewType {
                withItems(intItems)
                withLayoutResId(android.R.layout.list_content)
                bind<Int> {
                    assertTrue(intItems.contains(it))
                    intBindFunctionSpy(it)
                }
            }

            withViewType {
                withItems(anyItems)
                withLayoutResId(android.R.layout.list_content)
                bind<Any> {
                    assertTrue(anyItems.contains(it))
                    anyBindFunctionSpy(it)
                }
            }
        }
    }

    @Test
    fun t1_viewTypeAdapterPopulateTest() {
        val adapter = populateRecyclerView()

        activityController.create().start().visible()
        Mockito.verify(stringBindFunctionSpy, VerificationModeFactory.times(stringItems.size))
            .invoke(anyNullableObject(), anyNullableObject())
        Mockito.verify(intBindFunctionSpy, VerificationModeFactory.times(intItems.size)).invoke(
            anyNullableObject(),
            ArgumentMatchers.anyInt()
        )
        Mockito.verify(anyBindFunctionSpy, VerificationModeFactory.times(anyItems.size))
            .invoke(anyNullableObject(), anyNullableObject())

        val itemsByStringType = adapter.getItemsByType<String>()
        assertEquals(stringItems, itemsByStringType)
    }

    @Test
    fun t2_viewTypeAdapterUpdateTest() {
        val adapter = populateRecyclerView()
        assertNotNull(adapter)

        val topList = mutableListOf("x", "y", "z")
        val bottomList = mutableListOf(11, 22, 33, 44)

        adapter update {
            insertTop(topList)
            insertBottom(bottomList)
        }

        activityController.create().start().visible()

        Mockito.verify(stringBindFunctionSpy, VerificationModeFactory.times(stringItems.size))
            .invoke(anyNullableObject(), anyNullableObject())
        Mockito.verify(intBindFunctionSpy, VerificationModeFactory.times(intItems.size))
            .invoke(anyNullableObject(), anyInt())

        resetSpys()
        val removeItems = mutableListOf("x", "y")
        adapter update {
            removeItems(removeItems)
        }

        activityController.stop().create().start().visible()

        Mockito.verify(stringBindFunctionSpy, VerificationModeFactory.times(stringItems.size))
            .invoke(anyNullableObject(), anyNullableObject())

        resetSpys()
        val insertList = mutableListOf("insert1", "insert2")
        val insertIndex = 2
        adapter update {
            insert(insertIndex, insertList)
        }

        activityController.stop().create().start().visible()

        assertTrue { stringItems[insertIndex] == insertList.first() }

        resetSpys()
        adapter update {
            removeAll<String>()
            removeAll<Int>()
            removeAll<Any>()
        }
        activityController.stop().create().start().visible()

        assertTrue { adapter.itemCount == 0 }
    }

    @Test
    fun t3_viewTypeAdapterSwapTest() {
        val adapter = populateRecyclerView()
        val newList = mutableListOf<Short>(1, 2, 3, 4, 5)
        adapter restructure {
            insertBottom("shortList") {
                withItems(newList.toMutableList())
                withLayoutResId(android.R.layout.list_content)
            }
        }
        adapter update {
            swap<Short>(0, 3)
        }

        activityController.stop().create().start().visible()

        assertTrue {
            val firstItem = adapter.getItemsByType<Short>("shortList")[0]
            val secondItem = adapter.getItemsByType<Short>("shortList")[3]
            firstItem == newList[3] && secondItem == newList[0]
        }

        adapter restructure {
            insertBottom {
                withItem(7.toShort())
                withLayoutResId(android.R.layout.list_content)
            }
            insertTop {
                withItem(8.toShort())
                withLayoutResId(android.R.layout.list_content)
            }
        }

        adapter update {
            swap<Short>(1, 4, "shortList")
        }

        assertTrue {
            val firstItem = adapter.getItemsByType<Short>("shortList")[1]
            val secondItem = adapter.getItemsByType<Short>("shortList")[4]
            firstItem == newList[4] && secondItem == newList[1]
        }
    }
}