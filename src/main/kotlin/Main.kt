package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.example.models.News
import org.example.response.getNews
import org.example.utils.NewsFileSaver.processAndSaveNews

val threadCount = 5
val workerDispatcher = newFixedThreadPoolContext(threadCount, "WorkerPool")
val wantCntPage = 10;

/**
 * Generates a sequence of page numbers starting from a given ID and incrementing by a specified step.
 * The sequence continues until it reaches or exceeds the total count of pages.
 *
 * @param id The starting page number.
 * @param step The step to increment the page number by in each iteration.
 * @param cntAll The total number of pages to reach or exceed.
 * @return A mutable list of page numbers generated based on the given step.
 */
fun generatePageSequence(id: Int, step: Int, cntAll : Int): MutableList<Int> {
    val pageNumbers = mutableListOf<Int>()

    var currentId = id
    while (currentId <= cntAll) {
        pageNumbers.add(currentId)
        currentId += step
    }

    return pageNumbers
}

/**
 * Main function to demonstrate retrieving, processing, and saving news items.
 */
fun main() = runBlocking {
    val channel = Channel<List<News>>(Channel.UNLIMITED)
    val filePath = "news_parallel.csv"
    val workers = (0 until threadCount).map { workerId ->
        launch(workerDispatcher) {
            for (page in generatePageSequence(workerId + 1, threadCount, wantCntPage)) {
                val news = getNews(page)
                channel.send(news)
            }
        }
    }
    val processor = launch(Dispatchers.IO) {
        processAndSaveNews(channel, filePath)
    }

    workers.forEach { it.join() }
    channel.close()
    processor.join()
}