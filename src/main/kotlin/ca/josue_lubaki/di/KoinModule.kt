package ca.josue_lubaki.di

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.datasource.impl.UserDataSourceImpl
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
        "mongodb+srv://$mongoDBUsername:$mongoDBPassword@cluster0.ccy72hr.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)

    single { db }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
}