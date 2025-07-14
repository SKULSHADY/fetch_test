package com.fetch.test.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fetch.test.domain.model.ListItem
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for Room database operations on Item entities.
 */
@Dao
interface ListItemDao {
    /**
     * Inserts a list of items into the database.
     * If an item with the same primary key already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ListItem>)

    /**
     * Retrieves all items from the database, ordered by listId and then by name.
     * Returns a Flow, which emits new lists whenever the data changes.
     */
    @Query("SELECT * FROM listItems ORDER BY listId ASC, name ASC")
    fun getAllItems(): Flow<List<ListItem>>

    /**
     * Deletes all items from the database.
     */
    @Query("DELETE FROM listItems")
    suspend fun deleteAllItems()
}