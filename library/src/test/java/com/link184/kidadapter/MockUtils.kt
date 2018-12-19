package com.link184.kidadapter

import org.mockito.Mockito
import org.mockito.Mockito.RETURNS_DEEP_STUBS

inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)
inline fun <reified T : Any> spy(): T = Mockito.spy(T::class.java)
fun <T : Any> spy(obj: T): T = Mockito.spy(obj)

inline fun <reified T : Any> deepMock(): T = Mockito.mock(T::class.java, RETURNS_DEEP_STUBS)

fun <T> anyNullableObject(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T