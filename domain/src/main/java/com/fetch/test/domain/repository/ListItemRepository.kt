package com.fetch.test.domain.repository

import com.fetch.test.domain.model.ListItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Item Repository.
 * Defines the contract for data operations on Item objects.
 */
interface ListItemRepository {
    /**
     * Retrieves a Flow of processed items.
     * This method implements the offline-first strategy.
     */
    fun getProcessedItems(): Flow<List<ListItem>>

    /**
     * Triggers a refresh of data from the primary source (e.g., network).
     * This method will typically update the local cache.
     */
    suspend fun refreshItems()
}