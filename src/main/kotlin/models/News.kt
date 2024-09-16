package org.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.utils.LocalDateSerializer
import java.time.LocalDate
import kotlin.math.exp
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KProperty1

/**
 * Represents a news item.
 *
 * @property id The unique identifier of the news item.
 * @property title The title of the news item.
 * @property place The place where the event occurred (nullable).
 * @property description The description of the news item.
 * @property siteUrl The URL of the news item on the KudaGo website (nullable).
 * @property favoritesCount The number of users who have added this news item to their favorites.
 * @property commentsCount The number of comments on the news item.
 * @property publicationDate The publication date of the news item.
 * @property rating The rating of the news item, calculated based on favorites and comments.
 */
@Serializable
data class News(
    val id: Int,
    val title: String,
    val place: Place? = null,
    val description: String,
    @SerialName("site_url") val siteUrl: String? = null,
    @SerialName("favorites_count") val favoritesCount: Int = 0,
    @SerialName("comments_count") val commentsCount: Int = 0,
    @SerialName("publication_date") @Serializable(LocalDateSerializer::class) val publicationDate: LocalDate

) {
    /**
     * Calculates the rating of the news item based on the number of favorites and comments.
     * The formula used is: `1 / (1 + exp(-(favoritesCount / (commentsCount + 1))))`.
     */
    val rating: Double
        get() = 1.0 / (1 + exp(-(favoritesCount / (commentsCount + 1)).toDouble()))
}

/**
 * Filters and sorts a list of news items to return the most rated news within a specified period.
 *
 * @param count The maximum number of news items to return.
 * @param period The range of publication dates to filter the news items.
 * @return A list of the most rated news items within the specified period.
 */
fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    return this
        .asSequence()
        .filter { news ->
            val publicationDate = news.publicationDate
            publicationDate in period
        }
        .sortedByDescending { it.rating }
        .take(count)
        .toList()
}

/**
 * Prints the details of a news item.
 *
 * @param news The news item to print.
 */
fun printNews(news: News) {
    val clazz = news::class

    clazz.memberProperties.forEach { property ->
        val name = property.name

        val value = (property as KProperty1<News, *>).get(news)?.toString() ?: "null"

        println("${name.replaceFirstChar { it.titlecase() }}: $value")
    }

    println("-".repeat(20))
}
