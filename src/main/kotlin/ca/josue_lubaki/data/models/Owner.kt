package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.response.owner.OwnerResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Owner(
    @BsonId val id: ObjectId = ObjectId(),
    val username: String,
    val firstName : String,
    val lastName : String,
    val email: String,
    val phone: String,
) {
    fun toResponse(): OwnerResponse {
        return OwnerResponse(
            id = id.toString(),
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
        )
    }

    fun toDomain(): Owner {
        return Owner(
            id = id,
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
        )
    }
}
