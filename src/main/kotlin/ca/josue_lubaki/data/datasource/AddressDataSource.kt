package ca.josue_lubaki.data.datasource

import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.response.address.AddressResponse

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
interface AddressDataSource {

    suspend fun getAllAddresses(): List<AddressResponse>
    suspend fun getAddressById(id: String): AddressResponse?
    suspend fun getAddressByStreetAndNumber(street : String, number : String) : AddressResponse?
    suspend fun insertAddress(address: Address): Boolean
    suspend fun updateAddress(address: Address): Boolean
    suspend fun deleteAddress(id: String): Boolean
}