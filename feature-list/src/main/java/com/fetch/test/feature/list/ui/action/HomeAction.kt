package com.fetch.test.feature.list.ui.action

/**
 * Defines the possible actions that can be performed on the Home screen.
 */
sealed interface HomeAction {
    data object OnRefreshClick : HomeAction
    data object OnToggleModeClick : HomeAction
    data class OnTabClick(val id: Int) : HomeAction
}
