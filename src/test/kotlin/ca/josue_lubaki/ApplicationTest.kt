package ca.josue_lubaki

import ca.josue_lubaki.data.datasource.HouseDataSource
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import ca.josue_lubaki.plugins.*
import io.ktor.client.call.*
import org.koin.java.KoinJavaComponent.inject

class ApplicationTest {
    private val datasource : HouseDataSource by inject(HouseDataSource::class.java)

    @Test
    fun `access root endpoint, assert correct information`() = testApplication {
        client.get("/").apply {
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = status
            )
            assertEquals(
                expected = "Welcome to HomeFinder App !",
                actual = body()
            )
        }
    }
}
