package com.fetch.test.feature.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.test.core.data.preferences.AppPreferences
import com.fetch.test.domain.usecase.GetListItemsUseCase
import com.fetch.test.feature.list.ui.state.HomeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * ViewModel for the HomeScreen.
 * It prepares and manages the data for the UI using StateFlow.
 */
class HomeViewModel(
    private val getItemsUseCase: GetListItemsUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        // Collect processed items from the UseCase (which gets from repo/DB/network)
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    isListMode = appPreferences.getListMode()
                        .stateIn(viewModelScope, SharingStarted.Eagerly, false).value
                )
            )
            getItemsUseCase() // Invoke the UseCase to get the Flow
                .catch { e ->
                    // Handle errors during data collection from UseCase/Repository
                    _state.emit(
                        state.value.copy(
                            error = e as Exception?,
                            isLoading = false
                        )
                    )
                }
                .collectLatest { processedItems ->
                    _state.emit(
                        state.value.copy(
                            items = processedItems,
                            listIds = processedItems.map { it.listId }.distinct(),
                            selectedListId = state.value.selectedListId
                                ?: processedItems.firstOrNull()?.listId,
                            selectedListItems = processedItems.filter {
                                it.listId == (state.value.selectedListId
                                    ?: processedItems.firstOrNull()?.listId)
                            },
                            isLoading = false,
                            error = null
                        )
                    )
                }
        }
        checkForUpdates()
    }

    /**
     * Starts a periodic refresh of data every 30 seconds.
     */
    private fun checkForUpdates() {
        viewModelScope.launch {
            while (true) {
                refreshData()
                delay(TimeUnit.SECONDS.toMillis(30))
            }
        }
    }

    /**
     * Initiates a refresh of data from the network via the UseCase.
     */
    fun refreshData() {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    isLoading = true,
                    error = null
                )
            )
        }

        viewModelScope.launch {
            try {
                getItemsUseCase.refreshItems()
            } catch (e: Exception) {
                // Handle any exceptions during network fetching
                _state.emit(
                    state.value.copy(
                        error = e
                    )
                )
            } finally {
                // Loading will be turned off by the collectLatest block when new data comes in
                // or if an error occurred and no data is available.
                if (state.value.error != null && state.value.items.isEmpty()) {
                    _state.emit(
                        state.value.copy(
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    /**
     * Selects a specific list ID to display in the UI.
     * @param listId The ID of the list to select.
     */
    fun selectListId(listId: Int) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    selectedListId = listId,
                    selectedListItems = state.value.items.filter { it.listId == listId }
                )
            )
        }
    }

    /**
     * Toggles the current list mode (list/grid).
     */
    fun toggleMode() {
        viewModelScope.launch {
            appPreferences.setListMode(state.value.isListMode.not())
            _state.emit(
                state.value.copy(
                    isListMode = state.value.isListMode.not()
                )
            )
        }
    }
}

