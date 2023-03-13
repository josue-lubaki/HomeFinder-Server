package ca.josue_lubaki.tools

import ca.josue_lubaki.data.models.ApiResponse
import ca.josue_lubaki.data.request.auth.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.litote.kmongo.coroutine.CoroutineCollection
import kotlin.properties.Delegates

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */


const val NEXT_PAGE_KEY = "nextPage"
const val PREV_PAGE_KEY = "prevPage"

class Utils {
    companion object {
        fun requestRegisterValidator(request: RegisterRequest): Boolean {
            return request.username.isBlank()
                    || request.password.isBlank()
                    || request.email.isBlank()
                    || request.firstName.isBlank()
                    || request.lastName.isBlank()
        }

        fun calculatePage(page: Int): Map<String, Int?> {
            val prevPage : Int? = if (page == 1) null else page - 1
            val nextPage : Int? = if (page == 5) null else page + 1

            return mapOf(
                PREV_PAGE_KEY to prevPage,
                NEXT_PAGE_KEY to nextPage
            )
        }

        fun <T : Any> pager(
            collection : CoroutineCollection<T>,
            page: Int,
            limit: Int
        ) = collection.find().skip((page - 1) * limit).limit(limit)

        suspend fun PipelineContext<Unit, ApplicationCall>.initPageAndLimit(): Pair<Int, Int> {
            var page by Delegates.notNull<Int>()
            var limit by Delegates.notNull<Int>()

            kotlin.runCatching {
                page = call.request.queryParameters["page"]?.toInt() ?: 1
                limit = call.request.queryParameters["limit"]?.toInt() ?: 5
                require(page > 0)
                require(limit in 1..20)
            }.onFailure {
                call.respond(
                    message = ApiResponse<Boolean>(success = false, message = "Invalid page number or limit"),
                    status = HttpStatusCode.BadRequest
                )
            }
            return Pair(page, limit)
        }
    }
}