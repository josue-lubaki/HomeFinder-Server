package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.response.house.ApiResponse
import ca.josue_lubaki.data.response.house.HouseResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface HouseDataSource {
    suspend fun getAllHouses(page : Int = 1, limit : Int = 5): ApiResponse
    suspend fun getHouseById(id: String): ApiResponse
    suspend fun getHouseByOwnerAndAddress(ownerId: String, addressId: String): ApiResponse
    suspend fun insertHouse(house: House): ApiResponse
    suspend fun updateHouse(house: House): ApiResponse
    suspend fun deleteHouse(id: String): ApiResponse
    suspend fun getHouseByAddressId(id: String): ApiResponse
}