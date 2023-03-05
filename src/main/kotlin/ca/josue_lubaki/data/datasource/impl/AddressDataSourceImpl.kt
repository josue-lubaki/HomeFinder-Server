package ca.josue_lubaki.data.datasource.impl

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.models.Address
import ca.josue_lubaki.data.response.address.AddressResponse
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
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

    override suspend fun getAllAddresses(): List<AddressResponse> {
        return addresses.find().toList().map { it.toResponse() }
    }

    override suspend fun getAddressById(id: String): AddressResponse? {
        return addresses.findOneById(ObjectId(id))?.toResponse()
    }

    override suspend fun getAddressByStreetAndNumber(street: String, number: String): AddressResponse? {
        return addresses.find(
            Address::street eq street,
            Address::number eq number
        ).first()?.toResponse()
    }

    override suspend fun insertAddress(address: Address): Boolean {
        return addresses.insertOne(address).wasAcknowledged()
    }

    override suspend fun updateAddress(address: Address): Boolean {
        return addresses.replaceOne(Filters.eq("_id", address.id), address).wasAcknowledged()
    }

    override suspend fun deleteAddress(id: String): Boolean {
        return addresses.deleteOne(Filters.eq("_id", ObjectId(id))).wasAcknowledged()
    }
}