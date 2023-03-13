package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.response.user.UserResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
@Serializable
data class User (
    @Contextual @BsonId val id: ObjectId = ObjectId(),
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role : Role = Role.USER,
    val salt: String
) {
    fun toResponse() : UserResponse {
        return UserResponse(
            id = id.toString(),
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            role = role.name,
            password = password,
            salt = salt
        )
    }
}