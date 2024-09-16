package org.example.response

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.models.News
import org.example.response.Constants.DEFAULT_LOCATION
import org.example.response.Constants.DEFAULT_ORDER_BY
import org.example.response.Constants.NEWS_FIELDS


@Serializable
data class NewsResponse(
    val results: List<News>
)

object Constants {
    const val BASE_URL = "https://kudago.com/public-api/v1.4/news/"
    const val PAGE_SIZE_PARAM = "page_size"
    const val ORDER_BY_PARAM = "order_by"
    const val LOCATION_PARAM = "location"
    const val FIELDS_PARAM = "fields"
    const val DEFAULT_ORDER_BY = "publication_date"
    const val DEFAULT_LOCATION = "spb"
    const val NEWS_FIELDS = "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
}


suspend fun getNews(count: Int = 100): List<News> {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    val response: HttpResponse = client.get(Constants.BASE_URL) {
        parameter(Constants.PAGE_SIZE_PARAM, count)
        parameter(Constants.ORDER_BY_PARAM, DEFAULT_ORDER_BY)
        parameter(Constants.LOCATION_PARAM, DEFAULT_LOCATION)
        parameter(Constants.FIELDS_PARAM, NEWS_FIELDS)
    }

    val newsResponse: NewsResponse = response.body()
    client.close()

    return newsResponse.results
}

