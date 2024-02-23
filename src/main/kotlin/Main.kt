import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

fun main(): Unit = runBlocking {
    val websites = listOf(
        "https://www.google.com",
        "https://www.facebook.com",
        "https://www.github.com",
        "https://www.twitter.com",
        "https://www.instagram.com",
        "https://teach.vibelab.ru/",
        "https://www.patreon.com/ru-RU",
        "https://www.youtube.com/",
        "https://vk.com/",
        "https://habr.com/"
    )
    val jobs = mutableListOf<Job>()
    for (website in websites) {
        val job = launch {
            val isAvailable = checkWebsite(website)
            val status = if (isAvailable) "доступен" else "недоступен"
            println("Сайт $website $status")
        }
        jobs.add(job)
    }
    jobs.forEach { it.join() }
}

suspend fun checkWebsite(url: String): Boolean = withContext(Dispatchers.IO) {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }
}