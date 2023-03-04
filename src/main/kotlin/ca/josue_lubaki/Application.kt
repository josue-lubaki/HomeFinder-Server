package ca.josue_lubaki

import ca.josue_lubaki.data.datasource.UserDataSource
import ca.josue_lubaki.data.datasource.impl.UserDataSourceImpl
import ca.josue_lubaki.data.models.User
import io.ktor.server.application.*
import ca.josue_lubaki.plugins.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()

}
