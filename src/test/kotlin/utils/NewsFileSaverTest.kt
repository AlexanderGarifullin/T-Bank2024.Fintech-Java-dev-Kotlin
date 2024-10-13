package utils

import org.assertj.core.api.Assertions.assertThat
import org.example.models.News
import org.example.models.Place
import org.example.utils.NewsFileSaver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import java.time.LocalDate

class NewsFileSaverTest {
    private val testFilePath = "test_news.csv"

    @Test
    fun `should throw IllegalArgumentException if file already exists`(@TempDir tempDir: Path) {
        // Arrange
        val existingFilePath = tempDir.resolve(testFilePath).toString()
        File(existingFilePath).writeText("Existing file content")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            NewsFileSaver.saveNews(existingFilePath, emptyList())
        }
    }

    @Test
    fun `should save news items to CSV file`(@TempDir tempDir: Path) {
        // Arrange
        val testFilePath = tempDir.resolve(testFilePath).toString()
        val newsItems = listOf(
            News(1, "News 1", Place(1), "Description 1", "http://example.com/1", 10, 5, LocalDate.of(2024, 1, 1)),
            News(2, "News 2", Place(null), "Description 2", "http://example.com/2", 20, 15, LocalDate.of(2024, 2, 1))
        )

        // Act
        NewsFileSaver.saveNews(testFilePath, newsItems)

        // Assert
        File(testFilePath).useLines { lines ->
            val lineList = lines.toList()

            assertThat(lineList).hasSize(3)

            File(testFilePath).useLines { lines ->
                val lineList = lines.toList()

                assertThat(lineList).hasSize(3)

                assertThat(lineList[0]).isEqualTo("CommentsCount,Description,FavoritesCount,Id,Place,PublicationDate,Rating,SiteUrl,Title")

                assertThat(lineList[1]).isEqualTo("5,Description 1,10,1,Place(id=1),2024-01-01,0.7310585786300049,http://example.com/1,News 1")
                assertThat(lineList[2]).isEqualTo("15,Description 2,20,2,Place(id=null),2024-02-01,0.7310585786300049,http://example.com/2,News 2")
            }
        }
    }
}