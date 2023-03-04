package ca.josue_lubaki

import io.ktor.server.application.*
import ca.josue_lubaki.plugins.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val mongoDBUsername= System.getenv("MONGODB_USERNAME")
    val mongoDBPassword = System.getenv("MONGODB_PASSWORD")
    val dbName = System.getenv("MONGODB_DB_NAME")
    val db = KMongo.createClient(
        "mongodb+srv://$mongoDBUsername:$mongoDBPassword@cluster0.ccy72hr.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)

    configureSerialization()
    configureRouting()
    configureMonitoring()
    configureSecurity()
}
