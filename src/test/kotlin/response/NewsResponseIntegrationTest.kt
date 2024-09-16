package response

import kotlinx.coroutines.runBlocking

import org.example.response.getNews
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class NewsResponseIntegrationTest {

    @Test
    fun `should retrieve a list of news items from the API`() = runBlocking {
        val result = getNews(count = 2)

        assertThat(result).isNotEmpty
        println(result)
    }
}
