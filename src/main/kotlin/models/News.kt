package org.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.utils.LocalDateSerializer
import java.time.LocalDate
import kotlin.math.exp
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KProperty1

/*
 * - id - идентификатор новости
 * - title - заголовок новости
 * - place - место, где произошло событие
 * - description - описание новости
 * - siteUrl - ссылка на страницу новости на сайте KudaGo
 * - favoritesCount - число пользователей, добавивших новость в избранное
 * - commentsCount - число комментариев
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
    val rating: Double
        get() = 1.0 / (1 + exp(-(favoritesCount / (commentsCount + 1)).toDouble()))
}


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

fun printNews(news: News) {
    val clazz = news::class

    clazz.memberProperties.forEach { property ->
        val name = property.name

        val value = (property as KProperty1<News, *>).get(news)?.toString() ?: "null"

        println("${name.replaceFirstChar { it.titlecase() }}: $value")
    }

    println("---------------")
}
