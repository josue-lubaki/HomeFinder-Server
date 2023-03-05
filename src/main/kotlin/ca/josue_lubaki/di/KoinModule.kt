package ca.josue_lubaki.di

import ca.josue_lubaki.data.datasource.AddressDataSource
import ca.josue_lubaki.data.datasource.HouseDataSource
import ca.josue_lubaki.data.datasource.OwnerDataSource
import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.datasource.impl.AddressDataSourceImpl
import ca.josue_lubaki.data.datasource.impl.HouseDataSourceImpl
import ca.josue_lubaki.data.datasource.impl.OwnerDataSourceImpl
import ca.josue_lubaki.data.datasource.impl.UserDataSourceImpl
import ca.josue_lubaki.data.models.Role
import ca.josue_lubaki.security.hashing.HashingService
import ca.josue_lubaki.security.hashing.SHA265HashingServiceImpl
import ca.josue_lubaki.security.token.JwtToken
import ca.josue_lubaki.security.token.TokenConfig
import ca.josue_lubaki.security.token.TokenService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */
val KoinModule = module {
    val mongoDBUsername= System.getenv("MONGODB_USERNAME")
    val mongoDBPassword = System.getenv("MONGODB_PASSWORD")
    val dbName = System.getenv("MONGODB_DB_NAME")
    val db = KMongo.createClient(
        "mongodb+srv://$mongoDBUsername:$mongoDBPassword@cluster1.ebbpcnk.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)

    single { db }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }

    single<HouseDataSource> {
        HouseDataSourceImpl(get())
    }

    single<OwnerDataSource> {
        OwnerDataSourceImpl(get())
    }

    single<AddressDataSource> {
        AddressDataSourceImpl(get())
    }

    // Token & Hashing Providers
    single<TokenService> { JwtToken() }
    single {
        TokenConfig(
            secret = System.getenv("JWT_SECRET"),
            issuer = System.getenv("JWT_ISSUER"),
            audience = System.getenv("JWT_AUDIENCE"),
            expiresIn = 1000L * 60L * 60L * 24L * System.getenv("JWT_EXPIRATION_TIME").toLong(),
            role = Role.USER
        )
    }
    single<HashingService> { SHA265HashingServiceImpl() }

}