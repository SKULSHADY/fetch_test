package com.fetch.test.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fetch.test.core.data.dao.ListItemDao
import com.fetch.test.domain.model.ListItem

/**
 * Room Database class for the Fetch App.
 * Defines the database version and entities.
 */
@Database(entities = [ListItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ListItemDao

    companion object {
        @Volatile // Ensures that changes to the INSTANCE are immediately visible to other threads
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the AppDatabase.
         * If the instance is null, it creates a new one.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fetch_database"
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
