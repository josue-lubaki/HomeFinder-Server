package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.Address

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface AddressDataSource {

    suspend fun getAllAddresses(): List<Address>
    suspend fun getAddressById(id: String): Address?
    suspend fun getAddressByStreetAndNumber(street : String, number : String) : Address?
    suspend fun insertAddress(address: Address): Boolean
    suspend fun updateAddress(address: Address): Boolean
    suspend fun deleteAddress(id: String): Boolean
}