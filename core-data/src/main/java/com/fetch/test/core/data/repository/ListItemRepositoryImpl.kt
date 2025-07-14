package com.fetch.test.core.data.repository

import android.util.Log
import com.fetch.test.core.data.api.ApiService
import com.fetch.test.core.data.dao.ListItemDao
import com.fetch.test.domain.model.ListItem
import com.fetch.test.domain.repository.ListItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Implementation of the ItemRepository interface.
 * Handles data fetching from network and local database, implementing offline-first.
 */
class ListItemRepositoryImpl(
    private val apiService: ApiService,
    private val listItemDao: ListItemDao
) : ListItemRepository {

    /**
     * Provides a Flow of processed items.
     * It first emits data from the local database (offline-first),
     * then attempts to fetch fresh data from the network, updates the database,
     * and the Flow automatically emits the new data.
     */
    override fun getProcessedItems(): Flow<List<ListItem>> = flow {
        // Emit data from the database immediately
        listItemDao.getAllItems().map { items ->
            items.filter { !it.name.isNullOrBlank() }
                .sortedWith(compareBy<ListItem> { it.listId }.thenBy { it.name })
        }.collect { processedDbItems ->
            emit(processedDbItems)
        }
    }.onStart {
        // Try to fetch from network and refresh database
        try {
            val networkItems = apiService.getItems()
            listItemDao.insertAll(networkItems)
        } catch (e: Exception) {
            // Log network error. The UI will continue to display database data if available.
            Log.e("ItemRepositoryImpl", "Network refresh failed: ${e.message}")
        }
    }

    /**
     * Explicitly triggers a refresh of data from the network.
     * This can be used for pull-to-refresh functionality.
     */
    override suspend fun refreshItems() {
        try {
            val networkItems = apiService.getItems()
            listItemDao.insertAll(networkItems)
        } catch (e: Exception) {
            // Re-throw the exception so the ViewModel can handle the loading/error state for explicit refresh
            throw e
        }
    }
}