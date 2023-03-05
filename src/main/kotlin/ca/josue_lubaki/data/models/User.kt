package ca.josue_lubaki.data.models

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
)