package org.example.models

import kotlinx.serialization.Serializable


/**
 * Represents a place where an event occurred.
 *
 * @property id The unique identifier of the place (nullable).
 */
@Serializable
data class Place(
    val id: Int?
)