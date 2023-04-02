package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.response.house.HouseDto
import org.reactivestreams.Publisher

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface HouseDataSource {
    suspend fun getAllHouses(page : Int = 1, limit : Int = 5): ApiResponse<HouseDto>
    suspend fun getHouseById(id: String): ApiResponse<HouseDto>
    suspend fun getHouseByOwnerAndAddress(ownerId: String, addressId: String): ApiResponse<HouseDto>
    suspend fun insertHouse(house: House): ApiResponse<HouseDto>
    suspend fun updateHouse(house: House): ApiResponse<HouseDto>
    suspend fun deleteHouse(id: String): ApiResponse<HouseDto>
    suspend fun getHouseByAddressId(id: String): ApiResponse<HouseDto>
    suspend fun getAllHousesByAddressId(id: String): ApiResponse<HouseDto>
    suspend fun getHouseSize() : Long
}