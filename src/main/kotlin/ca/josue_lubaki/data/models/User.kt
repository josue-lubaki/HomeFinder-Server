package ca.josue_lubaki.data.models

import ca.josue_lubaki.data.dto.UserDTO
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
data class User (
    @BsonId val id: ObjectId = ObjectId(),
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val salt: String
) {
    fun toDTO() = UserDTO (
        username = username,
        password = password,
        email = email,
        firstName = firstName,
        lastName = lastName
    )
}