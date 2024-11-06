package org.example.utils

import kotlinx.coroutines.channels.ReceiveChannel
import org.example.models.News
import java.io.File
import kotlin.reflect.full.memberProperties

/**
 * Object responsible for saving news items to a CSV file.
 */
object NewsFileSaver {

    /**
     * Processes news items received from a [ReceiveChannel] and saves them to a CSV file.
     *
     * @param channel The channel providing collections of news items to save.
     * @param filePath The path of the CSV file to save.
     */
    suspend fun processAndSaveNews(channel: ReceiveChannel<List<News>>, filePath: String) {
        File(filePath).printWriter().use { writer ->
            val (headers, propertyMapping) = generateCsvHeaderAndMapping(News::class)
            writer.println(headers.joinToString(","))

            for (newsList in channel) {
                for (newsItem in newsList) {
                    val values = headers.map { header ->
                        val propertyName = propertyMapping[header]
                        val property = News::class.memberProperties.find { it.name == propertyName }
                        property?.get(newsItem)?.toString()?.replace("\n", " ") ?: ""
                    }
                    writer.println(values.joinToString(","))
                }
            }
        }
    }

    /**
     * Saves a collection of [News] items to a CSV file.
     *
     * @param path The path of the CSV file to save.
     * @param news The collection of news items to save.
     * @throws IllegalArgumentException If the file already exists.
     */
    fun saveNews(path: String, news: Collection<News>) {
        checkFilePath(path)

        File(path).printWriter().use { writer ->
            val (headers, propertyMapping) = generateCsvHeaderAndMapping(News::class)
            writer.println(headers.joinToString(","))

            news.forEach { newsItem ->
                val values = headers.map { header ->
                    val propertyName = propertyMapping[header]
                    val property = News::class.memberProperties.find { it.name == propertyName }
                    property?.get(newsItem)?.toString()?.replace("\n", " ") ?: ""
                }
                writer.println(values.joinToString(","))
            }
        }
    }

    /**
     * Generates CSV headers and a mapping of header names to property names.
     *
     * @param clazz The class to generate headers and mapping for.
     * @return A pair containing a list of headers and a map of header names to property names.
     */
    private fun generateCsvHeaderAndMapping(clazz: kotlin.reflect.KClass<*>): Pair<List<String>, Map<String, String>> {
        val headerFields = clazz.memberProperties
            .map { it.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }

        val propertyMapping = clazz.memberProperties
            .associate { it.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } to it.name }

        return Pair(headerFields, propertyMapping)
    }

    /**
     * Checks if the file at the specified path already exists.
     *
     * @param path The path of the file to check.
     * @throws IllegalArgumentException If the file already exists.
     */
    private fun checkFilePath(path: String) {
        val file = File(path)

        if (file.exists()) {
            throw IllegalArgumentException(illegalArgumentMsg(path))
        }
    }

    /**
     * Generates an error message for an existing file path.
     *
     * @param path The path of the file.
     * @return The error message.
     */
    private fun illegalArgumentMsg(path: String): String {
        return "The file in the specified path already exists: $path"
    }
}