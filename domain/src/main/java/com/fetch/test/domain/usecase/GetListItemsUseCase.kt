package com.fetch.test.domain.usecase

import com.fetch.test.domain.model.ListItem
import com.fetch.test.domain.repository.ListItemRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase for fetching and refreshing items.
 * It abstracts the data retrieval logic from the ViewModel.
 */
class GetListItemsUseCase(private val repository: ListItemRepository) {

    /**
     * Returns a Flow of processed items from the repository.
     * This Flow will automatically handle offline-first and network refreshes.
     */
    operator fun invoke(): Flow<List<ListItem>> {
        return repository.getProcessedItems()
    }

    /**
     * Triggers an explicit refresh of items from the network.
     */
    suspend fun refreshItems() {
        repository.refreshItems()
    }
}