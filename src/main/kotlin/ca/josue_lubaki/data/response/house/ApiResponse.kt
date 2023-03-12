package ca.josue_lubaki.data.response.house

import kotlinx.serialization.Serializable

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-11
 */
@Serializable
data class ApiResponse (
    val success: Boolean,
    val message: String? = null,
    val prevPage: Int? = null,
    val nextPage: Int? = null,
    val data: List<HouseDto?> = emptyList(),
    val lastUpdated : Long? = null
)