package com.link184.kidadapter

import com.link184.kidadapter.typed.MultiAdapterDsl
import com.link184.kidadapter.typed.MultiTypeAdapter
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MultiAdapterTest {
    lateinit var activityController: ActivityController<RecyclerViewActivity>

    private val stringItems = mutableListOf("a", "b", "c", "d")
    private val stringBindFunction: MultiAdapterDsl.(String) -> Unit = { println(it) }
    private var stringBindFunctionSpy = spy(stringBindFunction)
    private val intItems = mutableListOf(1, 2, 3, 4, 5, 9)
    private val intBindFunction: MultiAdapterDsl.(Int) -> Unit = { println(it) }
    private var intBindFunctionSpy = spy(intBindFunction)
    private val anyItems = mutableListOf(Any(), Any(), Any(), Any())
    private val anyBindFunction: MultiAdapterDsl.(Any) -> Unit = { println(it) }
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

    private fun populateRecyclerView(): MultiTypeAdapter {
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

        adapter.update {
            insertTop(topList)
            insertBottom(bottomList)
        }

        activityController.create().start().visible()

        Mockito.verify(stringBindFunctionSpy, VerificationModeFactory.times(stringItems.size ))
            .invoke(anyNullableObject(), anyNullableObject())
        Mockito.verify(intBindFunctionSpy, VerificationModeFactory.times(intItems.size))
            .invoke(anyNullableObject(), anyInt())

        val removeItems = mutableListOf("x", "y")
        adapter.update {
            removeItems(removeItems)
        }

        Mockito.verify(stringBindFunctionSpy, VerificationModeFactory.times(stringItems.size ))
            .invoke(anyNullableObject(), anyNullableObject())

    }
}