package models

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.example.models.News
import org.example.models.Place
import org.example.models.getMostRatedNews
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.math.exp

class NewsKtTest {
    @Test
    fun `should calculate rating correctly`() {
        // Given
        val news = News(
            id = 1,
            title = "Sample News",
            place = Place(1),
            description = "Description",
            siteUrl = "http://example.com",
            favoritesCount = 10,
            commentsCount = 5,
            publicationDate = LocalDate.now()
        )

        // When
        val expectedRating = 1.0 / (1 + exp(-(10 / (5 + 1)).toDouble()))

        // Then
        assertThat(news.rating).isCloseTo(expectedRating, within(0.0001))
    }

    @Test
    fun `should return most rated news within period`() {
        // Given
        val news1 = News(
            id = 1,
            title = "Low Rated News",
            place = Place(1),
            description = "Description",
            siteUrl = "http://example.com",
            favoritesCount = 1,
            commentsCount = 1,
            publicationDate = LocalDate.of(2024, 1, 1)
        )

        val news2 = News(
            id = 2,
            title = "High Rated News",
            place = Place(2),
            description = "Description",
            siteUrl = "http://example.com",
            favoritesCount = 10,
            commentsCount = 1,
            publicationDate = LocalDate.of(2024, 1, 2)
        )

        val news3 = News(
            id = 3,
            title = "Medium Rated News",
            place = Place(3),
            description = "Description",
            siteUrl = "http://example.com",
            favoritesCount = 5,
            commentsCount = 1,
            publicationDate = LocalDate.of(2024, 1, 3)
        )

        val newsList = listOf(news1, news2, news3)
        val dateRange = LocalDate.of(2024, 1, 1)..LocalDate.of(2024, 1, 3)

        // When
        val result = newsList.getMostRatedNews(count = 2, period = dateRange)

        // Then
        assertThat(result).hasSize(2)
            .containsExactly(news2, news3)
    }
}