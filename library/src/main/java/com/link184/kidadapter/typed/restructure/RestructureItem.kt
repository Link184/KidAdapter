package com.link184.kidadapter.typed.restructure

import com.link184.kidadapter.typed.AdapterViewTypeConfiguration

data class RestructureItem(
    val tag: String?,
    val configuration: AdapterViewTypeConfiguration.() -> Unit,
    val restructureType: RestructureType
)
