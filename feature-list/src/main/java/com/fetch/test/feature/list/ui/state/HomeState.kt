package com.fetch.test.feature.list.ui.state

import com.fetch.test.domain.model.ListItem
import java.lang.Exception

/**
 * Represents the state of the Home screen.
 */
data class HomeState(
    val items: List<ListItem> = emptyList(),
    val listIds: List<Int> = emptyList(),
    val selectedListId: Int? = null,
    val selectedListItems: List<ListItem> = emptyList(),
    val isListMode: Boolean = true,
    val isLoading: Boolean = false,
    val error: Exception? = null,
)