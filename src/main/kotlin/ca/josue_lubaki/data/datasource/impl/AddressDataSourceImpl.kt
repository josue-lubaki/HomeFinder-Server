package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.models.Address
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-05
 */
class AddressDataSourceImpl(
    db : CoroutineDatabase
) : AddressDataSource {

    private val addresses = db.getCollection<Address>()

    override suspend fun getAllAddresses(): List<Address> {
        return addresses.find().toList()
    }

    override suspend fun getAddressById(id: String): Address? {
        return addresses.findOneById(id)
    }

    override suspend fun getAddressByStreetAndNumber(street: String, number: String): Address? {
        return addresses.find(
            Address::street eq street,
            Address::number eq number
        ).first()
    }

    override suspend fun insertAddress(address: Address): Boolean {
        return addresses.insertOne(address).wasAcknowledged()
    }

    override suspend fun updateAddress(address: Address): Boolean {
        return addresses.updateOneById(address.id, address).wasAcknowledged()
    }

    override suspend fun deleteAddress(id: String): Boolean {
        return addresses.deleteOneById(id).wasAcknowledged()
    }
}