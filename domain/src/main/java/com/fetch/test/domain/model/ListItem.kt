package com.fetch.test.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing an item, now also a Room database entity.
 * @Serializable for Ktor's content negotiation (JSON parsing).
 * @Entity for Room, defining the table name and primary key.
 */
@Entity(tableName = "listItems")
@Serializable
data class ListItem(
    @PrimaryKey
    val id: Int,
    @SerialName("listId")
    val listId: Int,
    @SerialName("name")
    val name: String?
)