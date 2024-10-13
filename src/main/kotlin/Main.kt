package org.example

import kotlinx.coroutines.runBlocking
import org.example.models.getMostRatedNews
import org.example.models.printNews
import org.example.response.getNews
import org.example.utils.NewsFileSaver.saveNews
import java.time.LocalDate

/**
 * Main function to demonstrate retrieving, processing, and saving news items.
 */
fun main() = runBlocking {
    // Retrieve news from the API
    val newsList = getNews(10)
    println("Original News List:")
    newsList.forEach { news ->
        printNews(news)
    }

    // Define the date range for filtering
    val dateRange = LocalDate.of(2012, 12, 1)..LocalDate.of(2012, 12, 10)

    // Check the getMostRatedNews function
    val mostRatedNews = newsList.getMostRatedNews(count = 3, period = dateRange)
    println("Most Rated News:")
    mostRatedNews.forEach { news ->
        printNews(news)
    }

    // Path to the file where news will be saved
    val filePath = "news.csv"

    // Save news to a CSV file
    saveNews(filePath, newsList)
}