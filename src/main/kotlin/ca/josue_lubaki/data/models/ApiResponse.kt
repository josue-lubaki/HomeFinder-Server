package ca.josue_lubaki.data.models

import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-11
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val prevPage : Int? = null,
    val nextPage : Int? = null,
    val data: List<T?> = emptyList(),
    val lastUpdated : Long? = System.currentTimeMillis()
)