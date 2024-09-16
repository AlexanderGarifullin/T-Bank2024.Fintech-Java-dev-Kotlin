package org.example

import kotlinx.coroutines.runBlocking
import org.example.models.getMostRatedNews
import org.example.models.printNews
import org.example.response.getNews
import org.example.utils.NewsFileSaver.saveNews
import java.time.LocalDate

fun main() = runBlocking {
    // Получение новостей из API
    val newsList = getNews(10)
    println("Original News List:")
    newsList.forEach { news ->
        printNews(news)
    }

    // Определение диапазона дат для фильтрации
    val dateRange = LocalDate.of(2012, 12, 1)..LocalDate.of(2012, 12, 10)

    // Проверка функции getMostRatedNews
    val mostRatedNews = newsList.getMostRatedNews(count = 3, period = dateRange)
    println("Most Rated News:")
    mostRatedNews.forEach { news ->
        printNews(news)
    }

    // Путь к файлу, в который будут сохранены новости
    val filePath = "news.csv"

    // Сохранение новостей в CSV-файл
    saveNews(filePath, newsList)
}