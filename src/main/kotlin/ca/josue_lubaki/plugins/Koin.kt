package ca.josue_lubaki.plugins

import ca.josue_lubaki.di.KoinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

fun Application.configureKoin(){
    install(Koin){
        slf4jLogger()
        modules(KoinModule)
    }
}