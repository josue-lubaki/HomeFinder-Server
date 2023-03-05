package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.models.House
import ca.josue_lubaki.data.models.Owner
import ca.josue_lubaki.data.response.house.HouseResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface HouseDataSource {
    suspend fun getAllHouses(): List<HouseResponse>
    suspend fun getHouseById(id: String): HouseResponse?
    suspend fun getHouseByOwnerAndAddress(owner: Owner, address: Address): HouseResponse?
    suspend fun insertHouse(house: House): Boolean
    suspend fun updateHouse(house: House): Boolean
    suspend fun deleteHouse(id: String): Boolean
}