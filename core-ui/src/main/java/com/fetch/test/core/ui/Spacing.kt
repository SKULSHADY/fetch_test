package com.fetch.test.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

object Spacings {
    val default = 24.dp
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 32.dp
    val extraLarge = 64.dp
}

val MaterialTheme.spacings
    get() = Spacings

val LocalSpacings = compositionLocalOf { MaterialTheme.spacings }
