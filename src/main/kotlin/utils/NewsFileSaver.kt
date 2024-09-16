package org.example.utils

import org.example.models.News
import java.io.File
import kotlin.reflect.full.memberProperties

object NewsFileSaver {

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

    private fun generateCsvHeaderAndMapping(clazz: kotlin.reflect.KClass<*>): Pair<List<String>, Map<String, String>> {
        val headerFields = clazz.memberProperties
            .map { it.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }

        val propertyMapping = clazz.memberProperties
            .associate { it.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } to it.name }

        return Pair(headerFields, propertyMapping)
    }



    private fun checkFilePath(path: String) {
        val file = File(path)

        if (file.exists()) {
            throw IllegalArgumentException(illegalArgumentMsg(path))
        }
    }

    private fun illegalArgumentMsg(path: String): String {
        return "The file in the specified path already exists: $path"
    }
}